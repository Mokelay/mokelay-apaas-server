package com.greatbee.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.auth.AuthType;
import com.greatbee.core.bean.server.API;
import com.greatbee.api.bean.server.APILego;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.view.APILegoView;
import com.greatbee.core.bean.view.APIView;
import com.greatbee.core.manager.TYCacheService;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组装API View的服务
 * <p/>
 * Author CarlChen
 * Date 2018-6-1
 */
public class APIContentService {
    /**
     * Build API View
     *
     * @param tyCacheService
     * @param apiAlias
     * @return
     * @throws DBException
     */
    public static APIView buildAPIView(TYCacheService tyCacheService, String apiAlias) throws DBException {
        APIView apiView = new APIView();
        API api = tyCacheService.getAPIByAlias(apiAlias);
        apiView.setApi(api);

        //组装Auth Type
        String authType = api.getAuthType();
        if (StringUtil.isValid(authType)) {
            JSONArray authArray = null;
            try {
                authArray = JSONArray.parseArray(authType);
            } catch (Exception e) {
                if (authType.contains(",")) {
                    //如果是多选  a,b 类型 而非 ["a","b"]类型
                    authArray = new JSONArray(Arrays.asList(authType.split(",")));
                } else {
                    authArray = new JSONArray();
                    authArray.add(authType);
                }
            }
            List<AuthType> authTypes = new ArrayList<>();
            for (int i = 0; i < authArray.size(); i++) {
                String authAlias = authArray.getString(i);
                authTypes.add(tyCacheService.getAuthTypeByAlias(authAlias));
            }
            apiView.setAuthTypes(authTypes);
        }

        //组装乐高
        String version = api.getVersion();
        if (StringUtil.isValid(version) && API.VERSION_2.equalsIgnoreCase(version)) {
            //如果是2.0，则走JSON配置
            apiView.setApiLegoViews(_buildJSONAPILego(api.getContent()));
        } else {
            apiView.setApiLegoViews(_buildDBAPILego(tyCacheService, api));
        }
        return apiView;
    }

    /**
     * 先从缓存中读取，再从数据库中读取
     *
     * @param tyCacheService
     * @param api
     * @return
     * @throws DBException
     */
    private static List<APILegoView> _buildDBAPILego(TYCacheService tyCacheService, API api) throws DBException {
        List<APILegoView> apiLegoViews = new ArrayList<APILegoView>();

        List<APILego> apiLegos = tyCacheService.getAPILegoList(api.getAlias());
        if (CollectionUtil.isValid(apiLegos)) {
            for (APILego apiLego : apiLegos) {
                APILegoView apiLegoView = new APILegoView();
                apiLegoView.setApiLego(apiLego);
                //并发的根本原因：缓存是map对象应用变量，后面组装条件是用的同一块内存地址，所以后面获取的inputField和outputField是前一个修改后的值（fieldValue）,从而引起并发的问题
                apiLegoView.setInputFields(cloneInputFields(tyCacheService.getInputFields(apiLego.getUuid())));
                apiLegoView.setOutputFields(cloneOutputFields(tyCacheService.getOutputFields(apiLego.getUuid())));
                apiLegoViews.add(apiLegoView);
            }
        }
        Collections.sort(apiLegoViews, new Comparator<APILegoView>() {
            @Override
            public int compare(APILegoView o1, APILegoView o2) {
                int o1Index = o1.getApiLego().getIndex();
                int o2Index = o2.getApiLego().getIndex();
                if (o1Index > o2Index) {
                    return 1;
                } else if (o1Index == o2Index) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return apiLegoViews;
    }

    /**
     * 通过JSON数据组装APILegoView
     * <p/>
     * -[
     * --{
     * ------id
     * ------apiAlias
     * ------legoAlias
     * ------oiAlias
     * ------index
     * ------uuid
     * ------disabled
     * ------attributes
     * ------description
     * ------inputFields:[{
     * ----------id
     * ----------name
     * ----------description
     * ----------apiLegoId
     * ----------fieldName
     * ----------constant
     * ----------ct
     * ----------ift
     * ----------fvt
     * ----------fromApiLegoId
     * ----------fromApiLegoUuid
     * ----------alias
     * ----------attributes
     * ----------validate
     * ----------connectorPath
     * ----------dt
     * ----------inputFieldDescribeId
     * ----------requestParamName
     * ----------sessionParamName
     * ----------fromApiLegoOutputFieldAlias
     * ----------fieldTpl
     * ----------uuid
     * ----------apiLegoUuid
     * ------},{},{}],
     * ------outputFields:[{
     * ----------id
     * ----------name
     * ----------description
     * ----------apiLegoId
     * ----------fieldName
     * ----------fieldValue
     * ----------oft
     * ----------response
     * ----------handle
     * ----------alias
     * ----------dt
     * ----------uuid
     * ----------apiLegoUuid
     * ----------outputFieldDescribeId
     * ------},{},{}]
     * --},{},{}
     * -]
     *
     * @param content
     * @return
     */
    private static List<APILegoView> _buildJSONAPILego(String content) {
        List<APILegoView> apiLegoViews = new ArrayList<APILegoView>();

        if (StringUtil.isValid(content)) {
            JSONArray jsonArr = null;
            try {
                jsonArr = JSONArray.parseArray(content);
            } catch (JSONException e) {
                e.printStackTrace();
                return apiLegoViews;
            }
            for (Object o : jsonArr) {
                JSONObject json = (JSONObject) o;

                APILegoView apiLegoView = new APILegoView();

                //组装APILego
                APILego apiLego = new APILego();
                apiLego.setId(json.getInteger("id"));
                apiLego.setApiAlias(json.getString("apiAlias"));
                apiLego.setLegoAlias(json.getString("legoAlias"));
                apiLego.setOiAlias(json.getString("oiAlias"));
                apiLego.setIndex(json.getInteger("index"));
                apiLego.setUuid(json.getString("uuid"));
                apiLego.setAttributes(json.getString("attributes"));
                apiLego.setDescription(json.getString("description"));
                apiLego.setDisabled(DataUtil.getBoolean(json.getBoolean("disabled"),false));
                apiLegoView.setApiLego(apiLego);

                //组装Inputs
                List<InputField> inputFields = new ArrayList<InputField>();
                JSONArray inputJSON = json.getJSONArray("inputFields");
                if (inputJSON != null) {
                    for (Object p : inputJSON) {
                        JSONObject i = (JSONObject) p;
                        InputField inputField = new InputField();
                        inputField.setId(i.getInteger("id"));
                        inputField.setName(i.getString("name"));
                        inputField.setDescription(i.getString("description"));
                        inputField.setApiLegoId(i.getInteger("apiLegoId"));
                        inputField.setFieldName(i.getString("fieldName"));
                        inputField.setConstant(i.getString("constant"));
                        inputField.setCt(i.getString("ct"));
                        inputField.setIft(i.getString("ift"));
                        inputField.setFvt(i.getString("fvt"));
                        inputField.setFromApiLegoId(i.getInteger("fromApiLegoId"));
                        inputField.setFromApiLegoUuid(i.getString("fromApiLegoUuid"));
                        inputField.setAlias(i.getString("alias"));
                        inputField.setAttributes(i.getString("attributes"));
                        inputField.setValidate(i.getString("validate"));
                        inputField.setConnectorPath(i.getString("connectorPath"));
                        inputField.setDt(i.getString("dt"));
                        inputField.setInputFieldDescribeId(i.getInteger("inputFieldDescribeId"));
                        inputField.setRequestParamName(i.getString("requestParamName"));
                        inputField.setSessionParamName(i.getString("sessionParamName"));
                        inputField.setFromApiLegoOutputFieldAlias(i.getString("fromApiLegoOutputFieldAlias"));
                        inputField.setFieldTpl(i.getString("fieldTpl"));
                        inputField.setUuid(i.getString("uuid"));
                        inputField.setApiLegoUuid(i.getString("apiLegoUuid"));
                        inputFields.add(inputField);
                    }
                }
                apiLegoView.setInputFields(inputFields);

                //组装Outputs
                List<OutputField> outputFields = new ArrayList<OutputField>();
                JSONArray outputJSON = json.getJSONArray("outputFields");
                if (outputJSON != null) {
                    for (Object p : outputJSON) {
                        JSONObject i = (JSONObject) p;
                        OutputField outputField = new OutputField();
                        outputField.setId(i.getInteger("id"));
                        outputField.setName(i.getString("name"));
                        outputField.setDescription(i.getString("description"));
                        outputField.setApiLegoId(i.getInteger("apiLegoId"));
                        outputField.setFieldName(i.getString("fieldName"));
                        outputField.setOft(i.getString("oft"));
                        Boolean response = i.getBoolean("response");
                        outputField.setResponse(response == null ? false : response);
                        outputField.setHandle(i.getString("handle"));
                        outputField.setAlias(i.getString("alias"));
                        outputField.setDt(i.getString("dt"));
                        outputField.setUuid(i.getString("uuid"));
                        outputField.setApiLegoUuid(i.getString("apiLegoUuid"));
                        outputField.setOutputFieldDescribeId(i.getInteger("outputFieldDescribeId"));
                        outputFields.add(outputField);
                    }
                }
                apiLegoView.setOutputFields(outputFields);

                apiLegoViews.add(apiLegoView);
            }
        }
        return apiLegoViews;
    }

    /**
     * 从文件中读取APILegoView
     *
     * @param apiAlias
     * @return
     */
    private static List<APILegoView> _buildFileAPILego(String apiAlias) {
        try {
            StringBuilder filePath = new StringBuilder();
            filePath.append("ty/api").append(apiAlias);
            String content = new BufferedReader(new InputStreamReader(new ClassPathResource(filePath.toString()).getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            return _buildJSONAPILego(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 复制inputField 数组
     *
     * @param inputFields
     * @return
     */
    private static List<InputField> cloneInputFields(List<InputField> inputFields) {
        List<InputField> newIfs = new ArrayList<>();
        for (InputField inputField : inputFields) {
            newIfs.add(inputField.clone());
        }
        return newIfs;
    }

    /**
     * 复制outputField 数组
     *
     * @param outputFields
     * @return
     */
    private static List<OutputField> cloneOutputFields(List<OutputField> outputFields) {
        List<OutputField> newofs = new ArrayList<>();
        for (OutputField outputField : outputFields) {
            newofs.add(outputField.clone());
        }
        return newofs;
    }
}