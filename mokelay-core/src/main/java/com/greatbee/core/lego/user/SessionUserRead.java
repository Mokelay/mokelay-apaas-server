package com.greatbee.core.lego.user;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session用户信息读取
 * <p/>
 * 主要读取rs登录用户信息和TY用户信息(E端)
 * <p/>
 * 输入 （暂无）
 * <p/>
 * 输出
 * 1. 用户ID
 * 2. 用户姓名
 */
@Component("sessionUserRead")
public class SessionUserRead implements Lego {
    private static final String E_SESSION_USER_DETAIL_KEY = "E_SESSION_USER_DETAIL_KEY";
    private static final String TY_SESSION_CONFIG_USER = "TY_SESSION_CONFIG_USER";//E端session
    private static final String TY_B_SESSION_CONFIG_USER = "TY_SESSION_B_USER";//B端session

    private static final String Input_Key_Session_Read_Type = "sessionType";//session的读取类型 eg: E/B   默认是先从E端获取，没有再从B端获取

    private static final String Output_Key_Tenant_SerialNumber = "tenant_serial_number";
    private static final String Output_Key_User_Unique_ID = "user_unique_id";
    private static final String Output_Key_User_XingMIng = "user_xingming";

    //针对红星的Session定制字段
    private static final String Output_Key_RS_MarketId = "market_id";//商场ID
    private static final String Output_Key_RS_MarketDesc = "market_desc";//商场名称

    @Override
    public void execute(Input input, Output output) throws LegoException {
        HttpServletRequest request = input.getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        String sessionType = input.getInputValue(Input_Key_Session_Read_Type);

        Object rsUser = session.getAttribute(E_SESSION_USER_DETAIL_KEY);
        Object tyUser = session.getAttribute(TY_SESSION_CONFIG_USER);
        Object tyBUser = session.getAttribute(TY_B_SESSION_CONFIG_USER);

        if (rsUser != null) {
            JSONObject u = JSONObject.parseObject(String.valueOf(rsUser));

            output.setOutputValue(Output_Key_Tenant_SerialNumber, "TY-0000002");
            output.setOutputValue(Output_Key_User_Unique_ID, u.getString("id"));
            output.setOutputValue(Output_Key_User_XingMIng, u.getString("name"));

            //商场信息
            output.setOutputValue(Output_Key_RS_MarketId, u.getString("marketId"));
            output.setOutputValue(Output_Key_RS_MarketDesc, u.getString("marketDesc"));
        } else if (tyUser != null) {
            JSONObject u = JSONObject.parseObject(String.valueOf(tyUser));
            output.setOutputValue(Output_Key_Tenant_SerialNumber, u.getString("tenantSerialNumber"));
            output.setOutputValue(Output_Key_User_Unique_ID, u.getString("alias"));
            output.setOutputValue(Output_Key_User_XingMIng, u.getString("xingMing"));
        }
        if("B".equalsIgnoreCase(sessionType)||(StringUtil.isInvalid(sessionType)&&tyUser==null)){
            //如果是返回B端session
            JSONObject u = JSONObject.parseObject(String.valueOf(tyBUser));
            output.setOutputValue(Output_Key_Tenant_SerialNumber, u.getString("tenantSerialNumber"));
            output.setOutputValue(Output_Key_User_Unique_ID, u.getString("alias"));
            output.setOutputValue(Output_Key_User_XingMIng, u.getString("xingMing"));
        }
    }

    public static void main(String args[]) {
        JSONObject o = JSONObject.parseObject("{a:1}");
        System.out.println(o);
    }
}