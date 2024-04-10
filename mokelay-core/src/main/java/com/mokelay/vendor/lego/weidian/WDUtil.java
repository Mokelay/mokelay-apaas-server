package com.mokelay.vendor.lego.weidian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.lego.LegoException;
import com.mokelay.vendor.lego.weidian.bean.Param;
import com.mokelay.db.util.HttpClientUtil;
import com.mokelay.vendor.utils.VendorExceptionCode;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * WDUtil
 * 微店接口工具类
 *
 * @author xiaobc
 * @date 18/10/13
 */
public class WDUtil {

    private static final Logger logger = Logger.getLogger(WDUtil.class);

    public static final String WD_Api_Host = "https://api.vdian.com/api";
    private static final String WD_Auth_Host = "https://oauth.open.weidian.com";


    private static String access_token;
    private static long lastUpdateToken = 0;
    private static long updateInterval = 1000 * 72000;//20小时更新一次，因为微店的获取access_token次数是有限制的，并且有效期是25小时

    private static CloseableHttpClient httpClient = null;
    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static RequestConfig defaultRequestConfig = null;

    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000);
        connectionManager.setDefaultMaxPerRoute(400);

        defaultRequestConfig =
                RequestConfig.custom().setSocketTimeout(3000)
                        .setConnectTimeout(3000)
                        .setConnectionRequestTimeout(5000).build();
        httpClient =
                HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(defaultRequestConfig)
                        .build();
    }

    /**
     * post
     * @param url
     * @param params
     * @return
     * @throws LegoException
     */
    private static String post(String url,Param... params) throws LegoException {
        HttpPost post = new HttpPost(url);
        if (params != null && params.length > 0) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.length);
            for (Param p : params) {
                parameters.add(new BasicNameValuePair(p.getName(), p.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(parameters, Charset.forName("utf-8")));
        }
        return httpExecute(post, url);
    }

    /**
     * 执行请求
     * @param request
     * @param url
     * @return
     * @throws LegoException
     */
    private static String httpExecute(HttpRequestBase request, String url) throws LegoException {
        request.setConfig(RequestConfig.copy(defaultRequestConfig).build());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response == null) {
                throw new LegoException("http response is null|url:" + url,VendorExceptionCode.Lego_Error_Weidian_Post_Error);
            }
            StatusLine status = response.getStatusLine();
            int code = status.getStatusCode();
            if (code != 200) {
                throw new LegoException("http response code is " + code + "|reason:" + status.getReasonPhrase() + "|url:" + url,VendorExceptionCode.Lego_Error_Weidian_Post_Error);
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new LegoException("http response entity is null|url:" + url,VendorExceptionCode.Lego_Error_Weidian_Post_Error);
            }
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (ClientProtocolException e) {
            logger.error(e);
            throw new LegoException(e.getMessage()+"http client protocol exception|url:" + url,VendorExceptionCode.Lego_Error_Weidian_Post_Error);
        } catch (IOException e) {
            logger.error(e);
            throw new LegoException(e.getMessage()+"http io exception|url:" + url,VendorExceptionCode.Lego_Error_Weidian_Post_Error);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * 对外提供的 post请求方法
     * @param url
     * @param publicParam
     * @param bizParam
     * @return
     * @throws LegoException
     */
    public static String executePostForString(String url, Param publicParam, Param bizParam) throws LegoException {
        try {
            logger.info("url:"+url);
            return post(url, publicParam, bizParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new LegoException("WeidianClient execute failed:"+e.getMessage(), VendorExceptionCode.Lego_Error_Weidian_Post_Error);
        }
    }

    /**
     *  获取微店商品信息列表  根据ids   最多100个 一次
     *  url:https://api.vdian.com/api?param={"ids":"2250077393","need_idno":"1"}&public={"access_token": "xxx","method":"weidian.get.items","version":"1.1"}
     *  res:
     *      {
             "status": {
                 "status_code": 0,
                 "status_reason": ""
             },
             "result": [{
                 "high_point_price": null,
                 "img_head": "https://si.geilicdn.com/vshop395640-1390204649-2.jpg",
                 "low_point_price": null,
                 "edit_time": "2018-03-14 17:46:01",
                 "sup_price": 0,
                 "item_detail": "[{\"type\":2,\"url\":\"https://si.geilicdn.com/bj-wd-1021175336-1511334286701-220940967_1080_1440.jpg\"},{\"type\":1,\"text\":\"text\"}]",
                 "merchant_code": "merchant_code_2",
                 "bg_cate_id": 50025990,
                 "high_point": null,
                 "id": "2250077393",
                 "sup_id": 0,
                 "stock": 22,
                 "service_flag": null,
                 "full_imgs": [
                     "https://si.geilicdn.com/vshop395640-1390204649-2.jpg",
                     "https://si.geilicdn.com/vshop395640-1390204649-1.jpg"
                 ],
                 "future_sold_time": null,
                 "is_only_for_buyer": 0,
                 "sold": "0",
                 "is_need_idno": "0",
                 "is_tax_rate": 0,
                 "item_comment": "itemupdatetest.",
                 "status": 1,
                 "is_cvs_item": 0,
                 "flag_bin": "0",
                 "point_price": null,
                 "sku": [],
                 "high_price": "0.22",
                 "is_point_price": 0,
                 "is_future_sold": 0,
                 "flag": "0",
                 "price": "0.22",
                 "update_time": "2018-03-14 17:46:01",
                 "spu_id": 0,
                 "item_name": "itemupdatetest.",
                 "low_point": null,
                 "buy_stock": 22,
                 "low_price": "0.22",
                 "titles": [
                     "图片1",
                     "图片0"
                 ],
                 "seller_id": 923284842,
                 "add_time": "2018-03-14 17:40:23",
                 "purchase_fee": 68,
                 "is_wzx_sup_item": 0,
                 "tax_rate": null
             }]
            }
     * @param ids
     * @param need_idno
     * @return
     * @throws LegoException
     */
    public static String weidianGetItems(String appId,String appSecret,String ids, String need_idno) throws LegoException {
        Map< String, Object> map = new HashMap< String, Object>();
        map.put("ids", ids);
        map.put("need_idno", need_idno);
        removeNullValue(map);
        return executePostForString(WD_Api_Host,
                new Param("public", buildPublicValue(appId,appSecret,"weidian.get.items", "1.1")),
                new Param("param", JSON.toJSONString(map)));
    }

    /**
     * 分页获取所有商品数据
     * @param appId
     * @param appSecret
     * @param status status=1或不传为在架商品(不包含供货商货源)，status=2为下架商品,4表示下架和在架商品,10供货商货源
     * @return
     * @throws LegoException
     */
    public static String weidianGetProductPage(String appId,String appSecret,String page_num, String page_size,Number status) throws LegoException {
        Map< String, Object> map = new HashMap< String, Object>();
        map.put("page_num", page_num);
        map.put("status", status);
        map.put("page_size", page_size);
        removeNullValue(map);
        return executePostForString(WD_Api_Host,
                new Param("public", buildPublicValue(appId, appSecret, "vdian.item.list.get", "1.1")),
                new Param("param", JSON.toJSONString(map)));
    }


    private static String buildPublicValue(String appId,String appSecret,String method, String version) throws LegoException {
        return buildPublicValue(appId,appSecret,method, version, "json");
    }

    private static String buildPublicValue(String appId,String appSecret,String method, String version, String format) throws LegoException {
        Map<String, String> map = new HashMap<String, String>(8);
        map.put("method", method);
        map.put("access_token", getAccessTokenString(appId,appSecret));
        map.put("version", version);
        map.put("format", format);
        map.put("lang", "java");
        return JSON.toJSONString(map);
    }

    private static Map<String, Object> removeNullValue(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue() == null) {
                it.remove();
            }
        }
        return map;
    }

    /**
     * 获取accesstoken的值，添加点击频率校验，以免暴力请求导致sdk不能使用
     *
     * @return
     * @throws Exception
     */
    public static String getAccessTokenString(String appId,String appSecret) throws LegoException {
        if (accessTokenInvalid()) {
            initAccessToken(appId, appSecret);
        }
        return access_token;
    }

    /**
     * 获取accessToken的url
     *
     * @return
     */
    private static String getAccessTokenUrl(String appId,String appSecret) {
        StringBuilder urlBuilder = new StringBuilder(WD_Auth_Host);
        urlBuilder.append("/token?");
        urlBuilder.append("grant_type=client_credential");
        urlBuilder.append("&appkey=").append(appId);
        urlBuilder.append("&secret=").append(appSecret);
        return urlBuilder.toString();
    }

    /**
     * 只需要access_token有值就可以了，不需要返回，获取access_token值
     *
     * @throws LegoException
     */
    private static void initAccessToken(String appId,String appSecret) throws LegoException {
        String url = getAccessTokenUrl(appId,appSecret);
        String httpResponse = null;
        try {
            httpResponse = HttpClientUtil.get(url, null).getResponseBody();
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        if (StringUtil.isInvalid(httpResponse)) {
            logger.error("[SimpleWxEnterpriseManager][initAccessToken][error] httpResponse is invalid!");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpResponse);

        if (jsonObject.containsKey("result") &&  jsonObject.getJSONObject("result").containsKey("access_token")) {
            access_token = jsonObject.getJSONObject("result").getString("access_token");
            //更新lastupdate时间戳,以免暴力请求导致sdk不能使用
            lastUpdateToken = System.currentTimeMillis();
            System.out.println(access_token);
        }
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

}


