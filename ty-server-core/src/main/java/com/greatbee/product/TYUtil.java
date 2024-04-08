package com.greatbee.product;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.auth.AuthType;
import com.greatbee.base.bean.constant.DT;
import com.greatbee.core.bean.constant.ExecuteStatus;
import com.greatbee.core.bean.constant.FVT;
import com.greatbee.api.bean.server.APILego;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.view.APILegoView;
import com.greatbee.core.bean.view.APIView;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.core.bean.view.RedirectConfig;
import com.greatbee.core.lego.*;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.service.APIContentService;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * TY Util
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/13
 */
public class TYUtil implements ExceptionCode {

    private static final Logger logger = Logger.getLogger(TYUtil.class);

    /**
     * 执行API ALias
     *
     * @param apiAlias
     * @param request
     * @param response
     * @param filterMethodCheck 是否过滤请求方法校验  默认false不过滤
     * @return
     */
    public static Response executeAPIAlias(String apiAlias, TYDriver tyDriver, HttpServletRequest request, HttpServletResponse response, boolean filterMethodCheck) {

        TYTime tyTime = new TYTime(apiAlias);
        Response tyResponse = new Response();

        APIView apiView;
        try {
            //通过apiAlias中组装ApiView
            apiView = APIContentService.buildAPIView(tyDriver.getTyCacheService(), apiAlias);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            tyResponse.setOk(false);
            tyResponse.setCode(e.getCode());
            tyResponse.setMessage(e.getMessage());
            tyTime.finish();
            return tyResponse;
        }
        if (apiView == null || apiView.getApi() == null || !apiView.getApi().isEnable()) {
            tyResponse.setOk(false);
            tyResponse.setCode(ERROR_API_NOT_VALID);
            tyResponse.setMessage("API不可用");
            tyTime.finish();
            return tyResponse;
        }
        if (CollectionUtil.isInvalid(apiView.getApiLegoViews())) {
            tyResponse.setOk(false);
            tyResponse.setCode(ERROR_API_NO_LEGO);
            tyResponse.setMessage("没有可以执行的乐高");
            tyTime.finish();
            return tyResponse;
        }

        //API的请求方法判断
        String method = apiView.getApi().getMethod();
        if (StringUtil.isValid(method) && request != null && !filterMethodCheck) {
            String reqMethod = request.getMethod();
            if (!reqMethod.equalsIgnoreCase(method)) {
                tyResponse.setOk(false);
                tyResponse.setCode(ERROR_API_METHOD_NOT_MATCH);
                tyResponse.setMessage("提交方式不匹配");
                tyTime.finish();
                return tyResponse;
            }
        }

        //权限判断
        List<AuthType> authTypes = apiView.getAuthTypes();
        if (CollectionUtil.isValid(authTypes)) {
            for (int i = 0; i < authTypes.size(); i++) {
                AuthType authType = authTypes.get(i);
                if (authType != null && StringUtil.isValid(authType.getJudgeAPIAlias())) {
                    String judgeAPIAlias = authType.getJudgeAPIAlias();
                    Response authResponse = TYUtil.executeAPIAlias(judgeAPIAlias, tyDriver, request, response, true);
                    if (authResponse == null || (!authResponse.isOk())) {
                        //没有通过权限认证
                        tyResponse.setOk(false);
                        tyResponse.setCode(authType.getNoAuthErrorCode());
                        tyResponse.setMessage("没有权限访问");
                        tyTime.finish();
                        return tyResponse;
                    }
                }
            }
        }

        //获取APILego列表
        List<APILegoView> apiLegoViews = apiView.getApiLegoViews();

        //生成上下文Context
        Context context = new Context();

        if (CollectionUtil.isValid(apiLegoViews)) {
            for (APILegoView apiLegoView : apiLegoViews) {
                APILego apiLego = apiLegoView.getApiLego();
                if(apiLego.isDisabled()){
                    //如果当前节点被禁用，则直接跳过
                    continue;
                }
                tyTime.beginAPILego(apiLego);

                //处理Input字段的值
                List<InputField> inputFields = apiLegoView.getInputFields();
                if (CollectionUtil.isValid(inputFields)) {
                    for (InputField inputField : inputFields) {
                        FVT fvt = FVT.getFVT(inputField.getFvt());
                        if (fvt == null) {
                            fvt = FVT.Constant;
                        }
                        if (FVT.Constant.equals(fvt)) {
                            //如果是常量，则直接通过constant来读取
                            inputField.setFieldValue(inputField.getConstant());
                        } else if (FVT.Request.equals(fvt)) {
                            //通过FVT来给InputField来设定FieldValue
                            //从Request中获取值
                            String paramName = inputField.getRequestParamName();
                            DT dt = DT.getDT(inputField.getDt());
                            if (DT.Multipart.equals(dt)) {
                                CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
                                if (resolver.isMultipart(request)) {
//                                    MultipartHttpServletRequest multiRequest = resolver.resolveMultipart(request);
                                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                                    inputField.setFieldValue(multiRequest.getFile(paramName));
                                }
                            } else {
                                inputField.setFieldValue(request.getParameter(paramName));
                            }
                        } else if (FVT.Session.equals(fvt)) {
                            //处理从Session中获取值
                            HttpSession session = request.getSession();
                            if (session != null) {
                                Object sessionValue = session.getAttribute(inputField.getSessionParamName());
                                if (sessionValue != null) {
                                    inputField.setFieldValue(sessionValue);
                                }
                            }
                        } else if (FVT.Output.equals(fvt)) {
                            //处理从Output的fields中读取值
                            String fromApiLegoOutputFieldAlias = inputField.getFromApiLegoOutputFieldAlias();
                            String fromApiLegoUuid = inputField.getFromApiLegoUuid();
                            Output output = context.getOutput(fromApiLegoUuid);
                            if (output != null && StringUtil.isValid(fromApiLegoOutputFieldAlias)) {
                                List<OutputField> outputFields = output.getOutputFields();
                                if (CollectionUtil.isValid(outputFields)) {
                                    for (OutputField outputField : outputFields) {
                                        String outputAliasName = outputField.getAlias();
                                        if (fromApiLegoOutputFieldAlias.equalsIgnoreCase(outputAliasName)) {
                                            Object fieldValue = outputField.getFieldValue();
                                            if (fieldValue != null) {
                                                inputField.setFieldValue(fieldValue);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (FVT.Template.equals(fvt)) {
                            //处理从模板中取值
                            String tpl = inputField.getFieldTpl();
                            if (StringUtil.isInvalid(tpl)) {
                                tyResponse.setOk(false);
                                tyResponse.setCode(ERROR_API_CONFIG_NO_TPL);
                                tyResponse.setMessage("接口配置错误");
                                tyTime.finish();
                                return tyResponse;
                            }
                            List<OutputField> outputFields = null;
                            if (tpl.contains(TY_INPUTFIELD_TPL_NODE)) {
                                //如果是从节点获取
                                String fromApiLegoUuid = inputField.getFromApiLegoUuid();
                                Output output = context.getOutput(fromApiLegoUuid);
                                if (output != null) {
                                    outputFields = output.getOutputFields();
                                }
                            }
                            try {
                                inputField.setFieldValue(LegoUtil.transferInputValue(tpl, LegoUtil.buildTPLParams(request, null, outputFields,null)));
                            } catch (LegoException e) {
                                e.printStackTrace();
                                tyResponse.setOk(false);
                                tyResponse.setCode(e.getCode());
                                tyResponse.setMessage(e.getMessage());
                                tyTime.finish();
                                return tyResponse;
                            }
                        }
                    }
                }

                //用LegoAlias，通过Sping IOC获取Lego的实例化对象
                Lego lego = null;
                try {
//                    lego = (Lego) LegoUtil.getApplicationContext(request).getBean(apiLego.getLegoAlias());
                    lego = (Lego) SpringContextUtil.getBean(apiLego.getLegoAlias());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //开始执行每个lego对象
                if (lego != null) {
                    //组装Input
                    Input input = new Input(request, response);
                    input.setApiLego(apiLego);
                    input.setInputFields(inputFields);

                    //组装Output
                    Output output = new Output(request, response, apiLegoView.getOutputFields());

                    int step = 0;//步骤，表示lego执行到哪步了，用于下面流程分支判断，具体是validate抛错、excute抛错，还是handle抛错

                    try {
                        //检验输入是否有效
                        input.validate();
                        step=1;
                        //执行Lego
                        lego.execute(input, output);
                        step=2;
                        //对Output进行处理
                        output.handle();
                        step=3;
                        //Output加入上下文
                        context.addContext(apiLego, output);

                        tyTime.endAPILego(apiLego);
                    } catch (LegoException e) {


                        //获取当前乐高的流程控制器，执行控制器获取执行状态
                        ExecuteStatus es = null;
                        try {
                            String pcAlais = apiLego.getProcessControllerAlias();
                            if (StringUtil.isInvalid(pcAlais)) {
                                pcAlais = ProcessController.Default_Process_Controller;
                            }
//                            ProcessController pc = (ProcessController) LegoUtil.getApplicationContext(request).getBean(pcAlais);
                            ProcessController pc = (ProcessController) SpringContextUtil.getBean(pcAlais);
                            es = pc.controller(input, output, context, e);
                        } catch (Exception e1) {
                            //获取流程控制器失败
                            e1.printStackTrace();
                            logger.error(e1.getMessage(), e1);
                        }

                        if (es == null) {
                            es = ExecuteStatus.Error;
                        }

                        if (ExecuteStatus.Interrupt.equals(es)) {
                            logger.info("流程终止:" + es);
                            if(step==2){
                                //表示handle抛错,handle抛错的流程控制需要将output放到context中,其他情况不放
                                //添加返回数据到response
                                context.addContext(apiLego, output);
                            }
                            tyTime.endAPILego(apiLego);
                            break;
                        } else if (ExecuteStatus.Error.equals(es)) {
                            e.printStackTrace();
                            logger.error(e.getMessage(), e);

                            tyResponse.setOk(false);
                            tyResponse.setCode(e.getCode());
                            tyResponse.setMessage(e.getMessage());
                            tyTime.endAPILego(apiLego);
                            tyTime.finish();
                            return tyResponse;
                        } else if (ExecuteStatus.Continue.equals(es)) {
                            logger.info("流程继续:" + es);
                            //添加返回数据到response   流程继续的情况，基本只会在validate中使用，这个时候apiLego是不希望将output设置到context的
//                            context.addContext(apiLego, output);
                            tyTime.endAPILego(apiLego);
                        }
                    }
                } else {
                    //异常处理
                    tyResponse.setOk(false);
                    tyResponse.setCode(ERROR_API_LEGO_CODE_NOT_FOUND);
                    tyResponse.setMessage("Lego执行器没有找到");
                    tyTime.endAPILego(apiLego);
                    tyTime.finish();
                    return tyResponse;
                }
            }

            //处理context，把response打入进去
            //]如果一旦发现reponse list中有stream对象，则只打入stream对象到http resposne中
            List<OutputField> fields = context.getResponseList();
            if (CollectionUtil.isValid(fields)) {
                for (OutputField field : fields) {
                    Object fieldValue = field.getFieldValue();
                    if (fieldValue instanceof FileStream && response != null) {
                        ((FileStream) fieldValue).download(response);
                        tyTime.finish();
                        return null;
                    } else if (fieldValue instanceof RedirectConfig && response != null) {
                        try {
                            response.sendRedirect(((RedirectConfig) fieldValue).getUrl());
                            tyTime.finish();
                            return null;
                        } catch (IOException e) {
                            e.printStackTrace();
                            tyResponse.setOk(false);
                            tyResponse.setMessage("Redirect Error:" + e.getMessage());
                        }
                    } else {
                        tyResponse.addData(field.getAlias(), fieldValue);
                    }
                }
            }
        }
        tyTime.finish();
        return tyResponse;
    }
}


class TYTime {
    private static final Logger logger = Logger.getLogger(TYTime.class);

    private String apiAlias;
    private long begin;

    private Map<String, long[]> apiLegoTime = new LinkedHashMap<String, long[]>();

    //初始化
    public TYTime(String apiAlias) {
        this.apiAlias = apiAlias;
        this.begin = System.currentTimeMillis();
    }

    //开始执行LegoAlias
    public void beginAPILego(APILego apiLego) {
        String key = apiLego.getId() + "_" + apiLego.getLegoAlias();

        apiLegoTime.put(key, new long[]{System.currentTimeMillis(), 0L});
    }

    //结束执行LegoAlias
    public void endAPILego(APILego apiLego) {
        String key = apiLego.getId() + "_" + apiLego.getLegoAlias();

        long[] t = apiLegoTime.get(key);
        t[1] = System.currentTimeMillis();
    }

    public void finish() {
        long end = System.currentTimeMillis();

        long totalTime = end - this.begin;
        StringBuilder loggerContent = new StringBuilder();
        //logger打出 apiAlias执行的总时间，下面再把每个APILEGO执行的时间打出来
        loggerContent.append("\n").append("|-----------------------------").append("\n");
        loggerContent.append("| 执行接口:" + this.apiAlias + ",执行总时间:" + totalTime + ".").append("\n");
        // 打印出所有API LEGO执行时间明细:apiLegoTime
        for (Map.Entry<String, long[]> entry : apiLegoTime.entrySet()) {
            loggerContent.append("|").append("\n");
            loggerContent.append("| 执行乐高:" + entry.getKey() + ",执行lego时间:" + (entry.getValue()[1] - entry.getValue()[0]) + ".").append("\n");
            loggerContent.append("|").append("\n");
        }
        loggerContent.append("|-----------------------------").append("\n");

        logger.info(loggerContent.toString());
    }
}