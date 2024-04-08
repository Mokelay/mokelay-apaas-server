package com.greatbee.core.lego.wx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.MD5Util;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.LegoException;
import com.greatbee.core.util.HttpClientUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * WxUtil jssdk
 *
 * @author xiaobc
 * @date 18/7/9
 */
public class WxUtil {

    private static final Logger logger = Logger.getLogger(WxUtil.class);

    protected static final long ERROR_LEFO_WX_Sign_Error = 300031L;

    private static final String WX_Open_Api_Host = "https://api.weixin.qq.com";

    private static final String WX_Merchant_Host = "https://api.mch.weixin.qq.com";

    private static final String Device_Info = "WEB";//PC网页或者公众号支付用WEB
    public final static String WEIXIN_ORDER_STATUS_SUCCESS = "SUCCESS";
    public final static String WEIXIN_API_SIGN_TYPE = "MD5";

    private static String access_token;
    private static long lastUpdateToken = 0;
    //    private long updateInterval = 1000 * 7200;
    private static long updateInterval = 1000 * 1000;

    /**
     * ************************************ 下面是 jssdk 接口
     */

    /**
     * controller层调用：@RequestMapping(value = "/getSignature")
     * <p/>
     * 获取signature
     *
     * @return pipelineContext
     */
    public static JSONObject getSignature(String signUrl,String appId,String appSecret) throws DBException {
        //get ticket
        String jsapi_ticket = _getTicket(appId,appSecret);
        logger.info("[getSignature]jsapi_ticket" + jsapi_ticket);
        //get sign
        String nonce_str = _createNonceStr();
        String timestamp = _createTimestamp();
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + signUrl;
        logger.info("[getSignature]signUrl=" + signUrl);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            //hex
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error(e);
            logger.error(e.getMessage());
            logger.error(e.toString());
            throw new DBException(e.getMessage(),ERROR_LEFO_WX_Sign_Error);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e);
            logger.error(e.getMessage());
            logger.error(e.toString());
            throw new DBException(e.getMessage(),ERROR_LEFO_WX_Sign_Error);
        }

        JSONObject sign = new JSONObject();
        sign.put("url", signUrl);
        sign.put("jsapi_ticket", jsapi_ticket);
        sign.put("nonceStr", nonce_str);
        sign.put("timestamp", timestamp);
        sign.put("signature", signature);
        sign.put("appId", appId);
        logger.info("[getSignature]signature=" + sign.toString());
        return sign;

    }

    /**
     * 获取ticket
     *
     * @return
     * @throws Exception
     */
    private static String _getTicket(String appId,String appSecret) throws DBException {
        String url = WX_Open_Api_Host + "/cgi-bin/ticket/getticket?access_token=" + getAccessTokenString(appId,appSecret)+"&type=jsapi";
        //get ticket
        String httpResponse = HttpClientUtil.get(url, null).getResponseBody();
        logger.info(httpResponse);
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("输出jsapi：" +"[SimpleWxEnterpriseManager][_getTicket][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        if (jsonObject.containsKey("ticket")) {
            return jsonObject.getString("ticket");
        } else {
            logger.error("[SimpleWxEnterpriseManager][_getTicket][error] httpResponse is invalid!");
            return null;
        }
    }

    /**
     * 获取accessToken的url
     *
     * @return
     */
    public static String getAccessTokenUrl(String appId,String appSecret) {
        StringBuilder urlBuilder = new StringBuilder(WX_Open_Api_Host);
        urlBuilder.append("/cgi-bin/token?");
        urlBuilder.append("grant_type=client_credential");
        urlBuilder.append("&appid=").append(appId);
        urlBuilder.append("&secret=").append(appSecret);
        return urlBuilder.toString();
    }

    /**
     * 只需要access_token有值就可以了，不需要返回，获取access_token值
     *
     * @throws LegoException
     */
    public static void initAccessToken(String appId,String appSecret) throws DBException {
        String url = getAccessTokenUrl(appId,appSecret);
        String httpResponse = HttpClientUtil.get(url,null).getResponseBody();
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("[SimpleWxEnterpriseManager][initAccessToken][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        if (jsonObject.containsKey("access_token")) {
            access_token = jsonObject.getString("access_token");
            //更新lastupdate时间戳,以免暴力请求导致sdk不能使用
            lastUpdateToken = System.currentTimeMillis();
            System.out.println(access_token);
        }
    }


    /**
     * 获取accesstoken的值，添加点击频率校验，以免暴力请求导致sdk不能使用
     *
     * @return
     * @throws Exception
     */
    public static String getAccessTokenString(String appId,String appSecret) throws DBException {
        if (accessTokenInvalid()) {
            initAccessToken(appId, appSecret);
        }
        return access_token;
    }
    /**
     * 校验，以免暴力请求导致sdk不能使用
     *
     * @return
     */
    private static boolean accessTokenInvalid() {
        if (StringUtil.isInvalid(access_token)) {
            return true;
        } else if (lastUpdateToken + updateInterval < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 拉取公众号的用户列表  openId列表
     * ["OPENID1","OPENID2"...]
     * @param appId
     * @param appSecret
     * @return
     */
    public static JSONArray getWxUserList(String appId,String appSecret) throws DBException {
        //从头开始拉取公众号的用户列表，返回openid
        String url = WX_Open_Api_Host + "/cgi-bin/user/get?access_token=" + getAccessTokenString(appId,appSecret)+"&next_openid=";
        //get ticket
        String httpResponse = HttpClientUtil.get(url, null).getResponseBody();
        logger.info(httpResponse);
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("输出jsapi：" +"[getWxUserList][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);
        if (jsonObject.containsKey("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            if(data.containsKey("openid")){
                JSONArray openIds = data.getJSONArray("openid");
                return openIds;
            }
        } else {
            logger.error("[SimpleWxEnterpriseManager][_getTicket][error] httpResponse is invalid!");
            return null;
        }
        return null;
    }


    /**
     * 创建随机数
     *
     * @return
     */
    private static String _createNonceStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建当前时间戳
     *
     * @return
     */
    private static String _createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 微信消息需要转码
     *
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


    /**
     * 统一下单接口  之前授权过，直接传openId
     * * 统一下单接口参数：
     * {
     *     appid:公众账号ID,
     *     mch_id:商户号,
     *     device_info:'WEB',
     *     nonce_str:随机字符串,
     *     sign:签名,
     *     sign_type:'MD5',
     *     body:商品描述,
     *     detail:商品详情,
     *     attach:附加数据,
     *     out_trade_no:商户订单号,
     *     fee_type:"CNY",//标价币种
     *     total_fee:标价金额,单位为分,
     *     spbill_create_ip:终端IP,
     *     time_start:交易起始时间,
     *     time_expire:交易结束时间	,
     *     goods_tag:订单优惠标记,
     *     notify_url:通知地址,
     *     trade_type:"JSAPI",//交易类型
     *     product_id:商品ID	,NATIVE时必传
     *     openid:用户标识
     * }
     *
     * @param params
     * params:{
     *     appid:'',
     *     serialNumber:'订单编号',
     *     mch_id:'商户id',
     *     notify_url:'通知地址',
     *     openid:'用户openid',
     *     total_fee:'总金额，单位元',
     *     trade_type:'交易类型，默认JSAPI',
     *     wx_api_secret:'微信api密钥',
     *     body:'商品描述'//没有就显示订单号
     * }
     * @return
     * @throws Exception
     */
    public static JSONObject weixinOrder(Map<String,Object> params) throws Exception{
        String url = WX_Merchant_Host + "/pay/unifiedorder";
        Map<String,String> map = _getWxOrderParam(params);
        JSONObject httpResponse = wxPost(url, map);
        //接口请求失败
        if(httpResponse.containsKey("return_code") && StringUtil.equals(httpResponse.get("return_code").toString(),"FAIL")){
            throw new LegoException(httpResponse.get("return_msg").toString(),300112L);
        }

        Map<String,String> payMap = new TreeMap<String,String>();

        //如果return_code 和result_code 都为SUCCESS时  说明请求成功
        if(httpResponse.containsKey("return_code")
                &&httpResponse.containsKey("result_code")
                &&StringUtil.equals(httpResponse.get("return_code").toString(),WEIXIN_ORDER_STATUS_SUCCESS)
                &&StringUtil.equals(httpResponse.get("result_code").toString(),WEIXIN_ORDER_STATUS_SUCCESS)){
            if(httpResponse.containsKey("prepay_id")){
                String prepayId = httpResponse.get("prepay_id").toString();
                payMap.put("package","prepay_id="+prepayId.trim());
            }
        }else{
            throw new LegoException("请求失败，"+httpResponse.get("return_msg"),300113L);
        }
        payMap.put("appId",DataUtil.getString(params.get("appid"),"").trim());
        String timeStamp = _createTimestamp();
        payMap.put("timeStamp",timeStamp.trim());//时间戳
        String nonce_str_tmp = _createNonceStr();
        String nonce_str = nonce_str_tmp.replaceAll("-","").substring(0, 32);//随机数
        payMap.put("nonceStr", nonce_str.trim());
        payMap.put("signType",WEIXIN_API_SIGN_TYPE);
        String paySign = _wxPaySign(payMap,DataUtil.getString(params.get("wx_api_secret"),"").trim());
        payMap.put("paySign", paySign.trim());

        JSONObject resultJson = JSONObject.parseObject(JSONObject.toJSONString(payMap));

        return resultJson;
    }

    /**
     * 微信支付接口  签名
     * @param map
     * @return
     */
    private static String _wxPaySign(Map<String,String> map,String wxApiSecret) throws Exception {
        String stringTmp;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        stringTmp = "appId=" + map.get("appId") +
                "&nonceStr=" + map.get("nonceStr") +
                "&package=" + map.get("package") +
                "&signType=" + map.get("signType") +
                "&timeStamp=" + map.get("timeStamp") +
                "&key=" + wxApiSecret.trim();
        logger.info("[getSignature]stringTmp=" + stringTmp);
        signature = MD5Util.getMD5(stringTmp.trim());
        return signature.toUpperCase();
    }

    /**
     * 获取统一下单接口 参数 包括签名   已经授权过一次，直接传给openid
     * @param params
     * params:{
     *     appid:'',
     *     serialNumber:'订单编号',
     *     mch_id:'商户id',
     *     notify_url:'通知地址',
     *     openid:'用户openid',
     *     total_fee:'总金额，单位元',
     *     trade_type:'交易类型，默认JSAPI',
     *     wx_api_secret:'微信api密钥',
     *     body:'商品描述'//没有就显示订单号
     * }
     * @return
     * @throws Exception
     */
    private static Map<String,String> _getWxOrderParam(Map<String,Object> params) throws Exception {
        //get sign
        String nonce_str_tmp = _createNonceStr();
        String nonce_str = nonce_str_tmp.replaceAll("-","").substring(0,32);

        Map<String,String> map = new TreeMap<String,String>();
        map.put("appid", DataUtil.getString(params.get("appid"),"").trim());
        map.put("body", DataUtil.getString(params.get("body"),DataUtil.getString(params.get("serialNumber"), "")).trim());
        map.put("device_info",Device_Info);
        map.put("mch_id", DataUtil.getString(params.get("mch_id"),"").trim());
        map.put("nonce_str",nonce_str);
        map.put("notify_url",DataUtil.getString(params.get("notify_url"),"").trim());
        map.put("openid", DataUtil.getString(params.get("openid"),"").trim());
        map.put("out_trade_no",DataUtil.getString(params.get("serialNumber"), "").trim());
//        map.put("spbill_create_ip",context.getRequest().getHttpServletRequest().getRemoteAddr().trim());
        map.put("spbill_create_ip","192.168.1.100");

        double moneyTmp = mul(DataUtil.getDouble(params.get("total_fee"), 0), 100);
        int money = DataUtil.getInt(moneyTmp,0);
        map.put("total_fee", String.valueOf(money));

        //    map.put("total_fee","1");
        map.put("trade_type",DataUtil.getString(params.get("trade_type"),"JSAPI"));

        String stringTmp;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        stringTmp = "appid=" + map.get("appid") +
                "&body=" + map.get("body") +
                "&device_info=" + Device_Info +
                "&mch_id=" + map.get("mch_id") +
                "&nonce_str=" + nonce_str +
                "&notify_url=" + map.get("notify_url") +
                "&openid=" + map.get("openid") +
                "&out_trade_no=" + map.get("out_trade_no") +
                "&spbill_create_ip=" + map.get("spbill_create_ip") +
                "&total_fee=" + map.get("total_fee") +
                "&trade_type=" + map.get("trade_type") +
                "&key=" + DataUtil.getString(params.get("wx_api_secret"),"").trim();
        logger.info("[getSignature]stringTmp=" + stringTmp);
        signature = MD5Util.getMD5(stringTmp);
        map.put("sign",signature.toUpperCase());
        return map;
    }


    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * WXPOST
     *
     * @param url
     * @param params
     * @return
     */
    public static JSONObject wxPost(String url, Map<String, String> params) throws DBException {
        logger.info("[http][request]url=" + url + ",params=" + JSONObject.toJSONString(params).toString());
        String xmlStr = _mapToXMLStr(params);
        String response = HttpClientUtil.sendPostBody(url, xmlStr);
        logger.info("[http][response]" + response);
        try {
            // String resStr = new String(response.getBytes("ISO-8859-1"),"UTF-8");
            return _xmlToJSON(response);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            System.out.println("[response][" + response.getBytes().length / 1000 + "KB]" + response);
        }
        return null;
    }

    /**
     * map对象转换成xml字符串
     *
     * @param map
     * @return
     */
    private static String _mapToXMLStr(Map<String, String> map) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!StringUtil.equals(entry.getKey(), "sign")) {
                Element elm = root.addElement(entry.getKey());
                elm.setText(entry.getValue());
            }
        }
        Element elms = root.addElement("sign");
        elms.setText(map.get("sign"));
        return document.asXML();
    }

    /**
     * map对象转换成xml字符串   不要特殊处理
     *
     * @param map
     * @return
     */
    private static String _mapToXMLStrNew(Map<String, String> map) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element elm = root.addElement(entry.getKey());
            elm.setText(entry.getValue());
        }
        return document.asXML();
    }

    /**
     * 解析微信传过来的 xml数据
     *
     * @param xmlStr
     * @return
     */
    private static JSONObject _xmlToJSON(String xmlStr) {
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            //获取文档的根节点
            Element root = document.getRootElement();
            Map<String, String> map = new HashMap<String, String>();
            //对某节点下的所有子节点进行遍历.
            for (Iterator it = root.elementIterator(); it.hasNext(); ) {
                Element element = (Element) it.next();
                map.put(element.getName(), element.getText());
            }
            return JSONObject.parseObject(JSONObject.toJSONString(map));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     */
    public static Map parseXmlToList(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            //获取文档的根节点
            Element root = document.getRootElement();
            Map<String, String> map = new HashMap<String, String>();
            //对某节点下的所有子节点进行遍历.
            for (Iterator it = root.elementIterator(); it.hasNext(); ) {
                Element element = (Element) it.next();
                map.put(element.getName(), element.getText());
            }
            return map;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


}
