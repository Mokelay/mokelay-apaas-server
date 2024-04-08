package com.greatbee.vendor.lego.wx;

import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * wxGetAuthUrl
 *
 * 微信获取authurl lego
 * return eg:https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
 *
 * 这个lego 适合做成接口授权：用户进来有session 直接过；无session，调用这个lego 获取url，并且返回code
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wxGetAuthUrl")
public class WxGetAuthUrl extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxGetAuthUrl.class);

    private static final long ERROR_LEGO_WX_Get_Auth_Url_Error = 300021L;

    private static final String Input_Key_Lego_Type="legoType";//legoType （auth,lego）分为授权(auth) 和 普通乐高(lego),auth则直接抛错，跳转到微信授权页面
    private static final String Input_Key_Wx_Redirect_Url = "redirectUrl";
    private static final String Input_Key_Wx_Scope = "scope";
    private static final String Input_Key_Wx_State = "state";

    private static final String Input_Key_Wx_Auth_Use_Agent = "wxAuthUseAgent";//微信授权是否使用代理，主要原因是因为微信授权域名只能配置一个，需要一个中间域名转发

    private static final String Output_Key_Wx_Auth_Url = "authUrl";//微信授权url

    private static final int UN_WX_Login_Code = -420;//-401 表示 微信没有登录，直接跳转到微信授权登录界面,前台在接口调用的地方处理微信环境下-401跳转

    private static final String WX_Login_Scope_Base = "snsapi_base";//静默授权 //snsapi_userinfo 用户确认授权

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String resultWxAuthUrl = null;
        String appId = input.getInputValue(Input_Key_Wx_Open_App_Id);
        String legoType = input.getInputValue(Input_Key_Lego_Type);
        String redirectUrl = input.getInputValue(Input_Key_Wx_Redirect_Url);
        String scope = input.getInputValue(Input_Key_Wx_Scope);
        String state = input.getInputValue(Input_Key_Wx_State);
        //微信授权代理地址
        String wxAuthUseAgent = input.getInputValue(Input_Key_Wx_Auth_Use_Agent);

        Map params = buildTplParams(input);
        state = LegoUtil.transferInputValue(state, params);//附带参数可能需要模板
        redirectUrl = LegoUtil.transferInputValue(redirectUrl, params);//可能多个环境不一样，所以这里需要支持模板

        try {
            resultWxAuthUrl = getAuthUrl(wxAuthUseAgent,appId, redirectUrl,(StringUtil.isInvalid(scope)?WX_Login_Scope_Base:scope),state);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LegoException("获取微信授权URL失败",ERROR_LEGO_WX_Get_Auth_Url_Error);
        }

        if(StringUtil.isInvalid(legoType)||"auth".equalsIgnoreCase(legoType)){
            //legoType 无效 或者等于auth
            throw new LegoException(resultWxAuthUrl,UN_WX_Login_Code);
        }else{
            output.setOutputValue(Output_Key_Wx_Auth_Url,resultWxAuthUrl);
        }
    }





}
