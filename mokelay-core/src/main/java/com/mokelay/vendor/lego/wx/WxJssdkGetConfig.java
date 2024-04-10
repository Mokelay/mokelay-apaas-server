package com.mokelay.vendor.lego.wx;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.vendor.lego.wx.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * WxJssdkGetConfig
 *
 * 微信获取jssdk配置对象 lego
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wxJssdkGetConfig")
public class WxJssdkGetConfig extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxJssdkGetConfig.class);

    private static final long ERROR_LEGO_WX_Sign_Url_Error = 300030L;

    private static final String Input_Key_WX_Js_Sdk_Sign_Url="sign_url";//签名url，签名用的url必须是调用JS接口页面的完整URL,eg:location.href.split('#')[0]

    private static final String Output_Key_Wx_Config = "wx_config";//jssdk wx config 对象

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String signUrl = input.getInputValue(Input_Key_WX_Js_Sdk_Sign_Url);//签名url
        String appId = input.getInputValue(Input_Key_Wx_Open_App_Id);
        String secret = input.getInputValue(Input_Key_WX_Open_Secret);
        if(StringUtil.isInvalid(signUrl)){
            //签名url无效
            throw new LegoException("签名url确实",ERROR_LEGO_WX_Sign_Url_Error);
        }
        if(StringUtil.isInvalid(appId)||StringUtil.isInvalid(secret)){
            throw new LegoException("微信参数缺失",ERROR_LEGO_WX_Params_Null);
        }

        try {
            JSONObject wxConfig = WxUtil.getSignature(signUrl, appId, secret);
            output.setOutputValue(Output_Key_Wx_Config,wxConfig);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException("WX签名失败",ERROR_LEFO_WX_Sign_Error);
        }

    }





}
