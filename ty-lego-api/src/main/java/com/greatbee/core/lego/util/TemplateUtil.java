package com.greatbee.core.lego.util;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TemplateUtil
 *
 * @author xiaobc
 * @date 18/4/21
 */
public class TemplateUtil {

    private static final Logger logger = Logger.getLogger(TemplateUtil.class);

    //请求参数错误
    private static final Integer ERROR_API_REQUEST_ENCODE = 100006;

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
     * 构建 transferInputValue 需要的map参数，目前是整个input对象
     *
     * @param input
     * @return
     */
    public static Map<String, Object> buildTransferMap(Input input) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("input", input);
        return map;
    }

    /**
     * 输入参数 支持模板解析，可以替代 fvt中的 request，session，output
     *
     * @param tpl
     * @param request
     * @param outputFields
     * @return
     * @throws LegoException
     */
    public static String transferInputFieldValue(String tpl, HttpServletRequest request, List<OutputField> outputFields) throws LegoException {
        if (StringUtil.isInvalid(tpl)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("request", getParameterMap(request));
        params.put("session", getSessionAttributeMap(request));
        params.put("node", getOutputFieldMap(outputFields));
        return transferInputValue(tpl, params);
    }

    /**
     * 将request里的所有请求参数转换成map对象
     *
     * @return 参数map
     * @throws Exception
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) throws LegoException {
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
    public static Map<String, Object> getSessionAttributeMap(HttpServletRequest request) throws LegoException {
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
    public static Map<String, Object> getOutputFieldMap(List<OutputField> outputFields) throws LegoException {
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
    public static boolean isJson(String content) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
