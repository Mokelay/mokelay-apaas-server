package com.greatbee.core.lego.wx;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * WxGetUserInfo
 *
 * 微信获取用户信息 lego
 * {    "openid":" OPENID",
     " nickname": NICKNAME,
     "sex":"1",
     "province":"PROVINCE"
     "city":"CITY",
     "country":"COUNTRY",
     "headimgurl":    "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
     "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
     "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
    }
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wxGetUserInfo")
public class WxGetUserInfo extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxGetUserInfo.class);

    private static final String Output_Key_WX_User_Info = "wxUserInfo";//返回微信userinfo

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String code = input.getRequest().getParameter("code");
//        String state = input.getRequest().getParameter("state");
        logger.info("[WxGetUserInfo] code = "+code);
        String appId = input.getInputValue(Input_Key_Wx_Open_App_Id);
        String secret = input.getInputValue(Input_Key_WX_Open_Secret);
        if(StringUtil.isInvalid(appId)||StringUtil.isInvalid(secret)){
            throw new LegoException("微信参数缺失",ERROR_LEGO_WX_Params_Null);
        }
        JSONObject userInfo = null;
        try {
            userInfo = this.getAuthUserInfo(code, appId, secret);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        output.setOutputValue(Output_Key_WX_User_Info,userInfo);

        //如果配置了其他输出，只要在userInfo中都返回
        java.util.List<OutputField> ofs = output.getOutputFields();
        for(OutputField of:ofs){
            if(userInfo.containsKey(of.getFieldName())&&of.getFieldValue()==null){
                of.setFieldValue(userInfo.getString(of.getFieldName()));
            }
        }
    }





}
