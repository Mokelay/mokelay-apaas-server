package com.greatbee.core.lego.wx;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.Charset;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.util.HttpClientUtil;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * SelectCustomSQL
 *
 * 微信授权（登录、注册） lego
 *
 * @author xiaobc
 * @date 18/6/21
 */
public abstract class WxAuth implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(WxAuth.class);

    protected static final long ERROR_LEGO_WX_Params_Null = 300022L;

    protected static final long ERROR_LEFO_WX_Sign_Error = 300031L;

    private static final String WX_Open_Host="https://open.weixin.qq.com";
    private static final String WX_Open_Api_Host = "https://api.weixin.qq.com";

    protected static final String Input_Key_Wx_Open_App_Id = "openAppId";//微信公众号appID（公众号的唯一标识）
    protected static final String Input_Key_WX_Open_Secret = "openSecret";//微信secret



    /**
     * 构建tpl模板需要的请求参数
     * @param input
     * @return
     * @throws LegoException
     */
    protected Map buildTplParams(Input input) throws LegoException {
        java.util.List ifs = input.getInputFields();
        Map<String,Object> params = LegoUtil.buildTPLParams(input.getRequest(),null,null,input);
        Iterator result = ifs.iterator();
        while(result.hasNext()) {
            InputField _if = (InputField)result.next();
            params.put(_if.getFieldName(), _if.getFieldValue());
        }
        return params;
    }

    /**
     * 获取授权authUrl
     *  wxAuthUseAgent 使用中间代理跳转（解决微信授权域名只能配置一个的问题）
     *
     *  不用代理地址：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     *  用代理地址：http://www.abc.com/xxx/get-weixin-code.html?appid=XXXX&scope=snsapi_base&state=hello-world&redirect_uri=http%3A%2F%2Fwww.xyz.com%2Fhello-world.html
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String getAuthUrl(String wxAuthUseAgent,String appId,String redirectUrl,String scope,String state) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder();
        if(StringUtil.isValid(wxAuthUseAgent)){
            urlBuilder.append(wxAuthUseAgent);
        }else{
            urlBuilder.append(WX_Open_Host);
            urlBuilder.append("/connect/oauth2/authorize");
        }
        urlBuilder.append("?");
        urlBuilder.append("appid=").append(appId);
        urlBuilder.append("&redirect_uri=").append(URLEncoder.encode(redirectUrl, Charset.UTF_8));
        urlBuilder.append("&response_type=code");
        urlBuilder.append("&scope=").append(scope);
        urlBuilder.append("&state=").append(state);
        urlBuilder.append("#wechat_redirect");
        return urlBuilder.toString();
    }


    /**
     * 获取openid 的url
     * @return
     */
    protected String getOpenIdUrl(String code,String appId,String openSecret) {
        StringBuilder urlBuilder = new StringBuilder(WX_Open_Api_Host);
        urlBuilder.append("/sns/oauth2/access_token?");
        urlBuilder.append("appid=").append(appId);
        urlBuilder.append("&secret=").append(openSecret);
        urlBuilder.append("&code=").append(code);
        urlBuilder.append("&grant_type=authorization_code");
        return urlBuilder.toString();
    }

    /**
     * 获取微信 open详情 包括授权access_token  openId 等信息
     *
       {
         "access_token":"ACCESS_TOKEN",
         "expires_in":7200,
         "refresh_token":"REFRESH_TOKEN",
         "openid":"OPENID",
         "scope":"SCOPE"
        }
     * @param code
     * @param appId
     * @param openSecret
     * @return
     * @throws DBException
     */
    protected JSONObject getOpenInfo(String code,String appId, String openSecret) throws DBException {
        String url = this.getOpenIdUrl(code,appId,openSecret);
        logger.info("[WxAuth][getOpenId][getOpenUrl] url=" + url);
        String httpResponse = HttpClientUtil.get(url,null).getResponseBody();
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("[WxAuth][getOpenId][error] httpResponse is invalid!");
        }
        logger.info("[WxAuth][getOpenId][info] httpResponse=" + httpResponse);
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        return  jsonObject;
    }
    /**
     * 获取openId
     * @param code
     * @return
     * @throws DBException
     */
    protected String getOpenId(String code,String appId,String openSecret) throws DBException {
        JSONObject jsonObject = getOpenInfo(code, appId, openSecret);
        if (jsonObject.containsKey("openid")) {
            return jsonObject.getString("openid");
        }
        return null;
    }

    /**
     * 根据access_token 和 openid 获取用户信息
     * @return
     */
    protected JSONObject getAuthUserInfo(String code,String appId,String openSecret) throws DBException {
        JSONObject tokenOpenId = getOpenInfo(code,appId,openSecret);
        String access_token = null;
        String openId = null;
        if (tokenOpenId.containsKey("access_token")) {
            access_token = tokenOpenId.getString("access_token");
        }
        if(tokenOpenId.containsKey("openid")){
            openId = tokenOpenId.getString("openid");
        }

        StringBuilder url = new StringBuilder(WX_Open_Api_Host);
        url.append("/sns/userinfo?");
        url.append("access_token="+access_token);
        url.append("&openid="+openId);
        url.append("&lang=zh_CN");

        String httpResponse = HttpClientUtil.get(url.toString(),null).getResponseBody();
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("[WxAuth][getAuthUserInfo][error] httpResponse is invalid!");
            return null;
        }
        JSONObject userinfo = JSONObject.parseObject(httpResponse);
        return userinfo;
    }


}
