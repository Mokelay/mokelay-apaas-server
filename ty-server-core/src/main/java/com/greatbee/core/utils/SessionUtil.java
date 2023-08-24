package com.greatbee.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.DataUtil;
import com.greatbee.core.lego.LegoException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * SessionUtils
 *
 * @author xiaobc
 * @date 17/10/10
 */
public class SessionUtil {

    private static Logger logger = Logger.getLogger(SessionUtil.class);

    public static final String E_SESSION_USER_DETAIL_KEY = "E_SESSION_USER_DETAIL_KEY";//urms 对应的session key

    public static final String E_SESSION_USER_OPEN_APP_RESOURCE_KEY = "E_SESSION_USER_OPEN_APP_RESOURCE_KEY";//用户开通的app列表 以及每个列表下面的资源列表

    public static final String E_SESSION_USER_ID = "id";//员工号  对应ty_user表   userAlias

    public static final String E_SESSION_USER_NAME = "name";//员工姓名  对应ty_user表  xingMing

    /**
     * 获取session中的值
     *
     * @param req
     * @param key
     * @return
     */
    public static String getStringValue(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        Object obj = session.getAttribute(key);
        return DataUtil.getString(obj, null);
    }

    /**
     * 获取session中的值
     *
     * @param req
     * @param key
     * @return
     */
    public static Object getObjValue(HttpServletRequest req, String key) throws LegoException {
        Object data = null;
        HttpSession session = req.getSession(false);
        Object obj = session.getAttribute(key);
        String objStr = DataUtil.getString(obj, null);
        if (objStr != null) {
            logger.info("[SessionRead] obj=" + DataUtil.getString(obj, "getString error"));
            if (objStr.contains("{") || objStr.contains("[")) {
                data = JSON.parseObject(objStr);
            } else {
                data = obj;
            }
        }
        return data;
    }

    /**
     * 获取某个key中的属性值
     *
     * @param req
     * @param key
     * @param attrName
     * @return
     */
    public static String getAttributeValue(HttpServletRequest req, String key, String attrName) throws LegoException {
        Object obj = getObjValue(req, key);
        if (obj == null) {
            return null;
        }
        JSONObject json = (JSONObject) obj;
        if (json.containsKey(attrName)) {
            return json.getString(attrName);
        }
        return null;
    }

}
