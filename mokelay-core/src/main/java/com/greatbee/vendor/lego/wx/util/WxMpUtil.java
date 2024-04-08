package com.greatbee.vendor.lego.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.LegoException;
import com.greatbee.db.util.HttpClientUtil;
import com.greatbee.vendor.utils.VendorExceptionCode;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * WxMpUtil   weixin mini program util
 *
 * 小程序工具类
 *
 * @author xiaobc
 * @date 18/7/9
 */
public class WxMpUtil {

    private static final Logger logger = Logger.getLogger(WxMpUtil.class);

    private static final String WX_Open_Api_Host = "https://api.weixin.qq.com";

    private static String mp_session_key;//小程序session key

    private static String mp_access_token;//小程序 access_token
    private static long mpLastUpdateToken = 0;//小程序最后更新时间
    private static long mpUpdateInterval = 1000 * 1000;//小程序token 失效时间

    /**
     * 只需要access_token有值就可以了，不需要返回，获取 小程序 access_token值  ==小程序专用==
     *
     * 小程序 和公众号的access_token 获取接口都是一样的
     *
     * @throws LegoException
     */
    public static void initMpAccessToken(String appId,String appSecret) throws DBException {
        String url = WxUtil.getAccessTokenUrl(appId, appSecret);
        String httpResponse = HttpClientUtil.get(url,null).getResponseBody();
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("[SimpleWxEnterpriseManager][initAccessToken][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        if (jsonObject.containsKey("access_token")) {
            mp_access_token = jsonObject.getString("access_token");
            //更新lastupdate时间戳,以免暴力请求导致sdk不能使用
            mpLastUpdateToken = System.currentTimeMillis();
            System.out.println(mp_access_token);
        }
    }
    /**
     * 获取小程序 accesstoken的值，添加点击频率校验，以免暴力请求导致sdk不能使用  ==小程序专用==
     *
     * @return
     * @throws Exception
     */
    public static String getMpAccessTokenString(String appId,String appSecret) throws DBException {
        if (mpAccessTokenInvalid()) {
            initMpAccessToken(appId, appSecret);
        }
        return mp_access_token;
    }
    /**
     * 校验，以免暴力请求导致sdk不能使用  ==小程序专用==
     *
     * @return
     */
    private static boolean mpAccessTokenInvalid() {
        if (StringUtil.isInvalid(mp_access_token)) {
            return true;
        } else if (mpLastUpdateToken + mpUpdateInterval < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 获取小程序二维码 图片流
     * 返回：
     * {
             "openid": "OPENID",
             "session_key": "SESSIONKEY",
         }
     * @param appId
     * @param appSecret
     * @param scene
     * @param page
     * @param hyaline
     * @return
     */
    public static InputStream getMiniQrCode(String appId,String appSecret,String scene,String page,boolean hyaline) throws LegoException {
        //获取小程序access_token
        String mpAccessToken = null;
        try {
            mpAccessToken = WxMpUtil.getMpAccessTokenString(appId, appSecret);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        InputStream inputStream = null;//输入流

        String url = WX_Open_Api_Host+"/wxa/getwxacodeunlimit?access_token="+mpAccessToken;
        Map<String,Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("path", page);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        param.put("is_hyaline",hyaline);
        System.out.println("调用生成微信URL接口传参:" + param);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(param);
        StringEntity entity;
        try {
            entity = new StringEntity(body);
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "UTF-8"));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LegoException("生成小程序二维码失败", VendorExceptionCode.Lego_Error_WX_Mp_Generate_Error);
        }
        return inputStream;
    }


    /**
     * 生成open对象，包含 openid  和 session_key
     * jsCode 是由小程序wx.login接口返回， 只会一次有效
     * @param appid
     * @param appSecret
     * @param jsCode
     * @return
     */
    public static JSONObject getOpenObj(String appid,String appSecret,String jsCode) throws LegoException {
        String getOpenUrl = WX_Open_Api_Host+"/sns/jscode2session?appid="+appid+"&secret="+appSecret+"&js_code="+jsCode+"&grant_type=authorization_code";
        String httpResponse = null;
        try {
            httpResponse = HttpClientUtil.get(getOpenUrl, null).getResponseBody();
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        logger.info(httpResponse);
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("输出小程序接口返回：" +"[getOpenObj][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        if(jsonObject.containsKey("session_key")){
            //调这个接口 即前端调用wx.login接口，刷新session_key
            mp_session_key = jsonObject.getString("session_key");
            return jsonObject;
        }
        return null;
    }

    /**
     * 获取小程序openId  或更新session_key
     * @param appid
     * @param appSecret
     * @param jsCode
     * @return
     */
    public static String getMpOpenId(String appid,String appSecret,String jsCode) throws LegoException {
        JSONObject obj = getOpenObj(appid, appSecret, jsCode);
        if(obj!=null){
            return obj.getString("openid");
        }
        return null;
    }

}
