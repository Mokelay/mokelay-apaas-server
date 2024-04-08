package com.greatbee.core.lego.util;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.constant.CT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.view.Condition;
import com.greatbee.db.bean.view.MultiCondition;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lego Util
 * <p/>
 * Author :CarlChen
 * Date:17/7/13
 */
public class LegoUtil implements ExceptionCode {

    private static final Logger logger = Logger.getLogger(LegoUtil.class);

    //列表增加序号
    private static final String List_Index_flag = "_index";

    /**
     * 获取IOC容器
     *
     * @param request
     * @return
     */
    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
        return WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
    }

    /**
     * 根据输入构建Condition
     *
     * @param input
     * @return
     */
    public static MultiCondition buildCondition(Input input) {
        MultiCondition result = new MultiCondition();
        List<InputField> conditionFields = input.getInputField(IOFT.Condition);
        if (CollectionUtil.isValid(conditionFields)) {
            for (InputField inputField : conditionFields) {
                Condition c = new Condition();
                c.setConditionFieldName(inputField.getFieldName());
                c.setConditionFieldValue(inputField.fieldValueToString());
                String ct = inputField.getCt();
                if (StringUtil.isInvalid(ct)) {
                    ct = CT.EQ.getName();
                }
                c.setCt(ct);
                result.addCondition(c);
            }
        }
        return result;
    }

    /**
     * 构建File对象，如果路径不存在就创建
     *
     * @param path
     * @return
     */
    public static File buildFile(String path) throws IOException {
        File _file = new File(path);
        File _parentFile = _file.getParentFile();
        if (!_parentFile.exists()) {
            _parentFile.mkdirs();
        }
        _file.createNewFile();
        return _file;
    }

    /**
     * 检查对象是否  不为空   用于validate
     *
     * @param obj
     * @return
     */
    public static boolean checkIsNotNull(Object obj) {
        boolean isNotNull = false;
        if (obj instanceof DataPage) {
            List<Data> list = ((DataPage) obj).getCurrentRecords();
            if (CollectionUtil.isValid(list)) {
                isNotNull = true;
            }
        } else if (obj instanceof DataList) {
            List<Data> list = ((DataList) obj).getList();
            if (CollectionUtil.isValid(list)) {
                isNotNull = true;
            }
        } else if (obj instanceof Data) {
            if (obj != null & ((Data) obj).size() > 0) {
                isNotNull = true;
            }
        } else if (obj == null) {
            isNotNull = false;
        } else {
            isNotNull = true;
        }
        return isNotNull;
    }

    /**
     * 判断string 是否是整数 数字类型
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 常用的模板表达式：${name}   ${user.name}  ${xxx.xxx.xxx.xxx}
     * freemarker 模板转换
     * 将表达式转换成正确的值
     *
     * @param inputval
     * @param map
     * @return
     */
    public static String transferInputValue(String inputval, Map<String, Object> map) throws LegoException {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setNumberFormat("#");//数值大于1000  不转换成金额格式
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("input", inputval);
            cfg.setTemplateLoader(stringLoader);

            Template template = cfg.getTemplate("input");

            StringWriter writer = new StringWriter();
            template.process(map, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("模板转换异常", 30001);
        } catch (TemplateException e) {
            e.printStackTrace();
            throw new LegoException("模板转换异常", 30001);
        }
    }

    /**
     * 统一组装TPL通用参数
     *
     * @param request
     * @param inputFields
     * @param outputFields
     * @param input
     * @return
     * @throws LegoException
     */
    public static Map<String, Object> buildTPLParams(
            HttpServletRequest request,
            List<InputField> inputFields,
            List<OutputField> outputFields,
            Input input) throws LegoException {
        Map<String, Object> params = new HashMap<>();

        if (request != null) {
            params.put("request", getParameterMap(request));
            params.put("session", getSessionAttributeMap(request));
            params.put("sessionId", request.getSession().getId());
        }

        if (CollectionUtil.isValid(outputFields)) {
            params.put("node", getOutputFieldMap(outputFields));
        }

        if (CollectionUtil.isValid(inputFields)) {
            for (InputField _if : inputFields) {
                params.put(_if.getFieldName(), _if.getFieldValue());
            }
        }

        if (input != null) {
            params.put("input", input);
        }

        return params;
    }

    /**
     * 将request里的所有请求参数转换成map对象
     *
     * @return 参数map
     * @throws Exception
     */
    private static Map<String, Object> getParameterMap(HttpServletRequest request) throws LegoException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            byte source[] = new byte[0];
            try {
                source = request.getParameter(key).getBytes("iso8859-1");
                String modelname = new String(source, "UTF-8");
                resultMap.put(key, modelname);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new LegoException("请求参数编码错误", ERROR_API_REQUEST_ENCODE);
            }
        }
        return resultMap;
    }

    /**
     * 将session里的所有参数转换成map对象
     *
     * @return 参数map
     * @throws Exception
     */
    private static Map<String, Object> getSessionAttributeMap(HttpServletRequest request) throws LegoException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpSession session = request.getSession();
        Enumeration<String> enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            Object value = session.getAttribute(key);
            if (value instanceof String && isJson(DataUtil.getString(value, null))) {
                JSONObject val = JSONObject.parseObject(DataUtil.getString(value, null));
                resultMap.put(key, val);
            } else {
                resultMap.put(key, session.getAttribute(key));
            }
        }
        return resultMap;
    }

    /**
     * 将前面节点的outputField里的所有参数转换成map对象
     *
     * @return 参数map
     * @throws Exception
     */
    private static Map<String, Object> getOutputFieldMap(List<OutputField> outputFields) throws LegoException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (outputFields != null) {
            for (OutputField of : outputFields) {
                Object obj = of.getFieldValue();
                if (obj instanceof String && isJson(DataUtil.getString(obj, null))) {
                    resultMap.put(of.getAlias(), JSONObject.parseObject(DataUtil.getString(obj, null)));
                } else {
                    resultMap.put(of.getAlias(), obj);
                }
            }
        }
        return resultMap;
    }

    /**
     * 判断一个string是否是json格式
     *
     * @param content
     * @return
     */
    private static boolean isJson(String content) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 给list 添加序号,如果是page 需要考虑分页的情况，其他直接序号
     *
     * @param o
     */
    public static void buildListIndex(Object o) {
        if (o == null) {
            return;
        }
        List<Map<Object, Object>> list = null;//需要加索引的list
        int _index = 1;//开始序号
        if (o instanceof DataList) {
            list = ((DataList) o).getList();
        } else if (o instanceof List) {
            list = (List) o;
        } else if (o instanceof DataPage) {
            list = ((DataPage) o).getCurrentRecords();
            int page = ((DataPage) o).getCurrentPage();
            int pageSize = ((DataPage) o).getPageSize();
            _index = (page - 1) * pageSize + _index;
        }
        if (list != null) {
            for (Map map : list) {
                map.put(List_Index_flag, _index++);
            }
        }
    }


}
