package com.greatbee.vendor.lego.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.api.lego.LegoException;
import com.greatbee.vendor.lego.utils.VendorUtil;
import com.greatbee.vendor.lego.wx.model.WxMessage;
import com.greatbee.db.util.HttpClientUtil;
import com.greatbee.vendor.utils.HttpFileUtil;
import com.greatbee.vendor.utils.VendorExceptionCode;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WxMsgUtil
 * 主要用于微信公众号消息图文推送，支持单点推送，也支持群发
 *
 * @author xiaobc
 * @date 18/9/10
 */
public class WxMsgUtil {

    private static final Logger logger = Logger.getLogger(WxUtil.class);

    // 图文内的图片上传接口地址
    private static String Wx_Msg_UploadImageUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
    // 图文封面图片上传接口地址
    private static String Wx_Msg_PostImageMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=image";
    // 图文素材上传接口地址
    private static String Wx_Msg_PostNewsUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
    // 群发接口地址
    private static String Wx_Msg_SendToAllUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
    // 根据openid列表 群发接口地址
    private static String Wx_Msg_SendUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";

    /**
     * 网络封面转成微信的mediaId
     * @param wm
     * @param appid
     * @param appSecret
     * @return
     * @throws LegoException
     */
    public static void setCoverImage(WxMessage wm,String appid,String appSecret) throws LegoException {
        String imgUrl = wm.getImageUrl();
        //转成本地临时文件
        JSONObject imgObj = VendorUtil.getLocalFileFromNetFile(imgUrl, null);
        String localPath = imgObj.getString("filePath");
        //上传封面到微信
        try {
            JSONObject res = HttpFileUtil.fileUpload(Wx_Msg_PostImageMediaUrl.replace("ACCESS_TOKEN", WxUtil.getAccessTokenString(appid, appSecret)), localPath);
            wm.setImageMediaId(res.getString("media_id"));
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("----------上传封面发生错误:{}."+e.getMessage());
            throw new LegoException(e.getMessage(),e.getCode());
        }
    }

    /**
     * 构建单个上传图文的map数据
     * @param wm
     * @return
     */
    private static Map<String, Object> buildMessage(WxMessage wm){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("thumb_media_id",wm.getImageMediaId());
        jsonMap.put("author",wm.getAuthor());
        jsonMap.put("title",wm.getTitle());
        jsonMap.put("content_source_url",wm.getContentSourceUrl());
        jsonMap.put("content",wm.getContent());
        jsonMap.put("show_cover_pic",wm.getShowCoverPic());
        return jsonMap;
    }

    /**
     * 构建微信上传图文消息 json数据
     * @param wms
     * @return
     */
    public static String buildFullMessage(List<WxMessage> wms){
        Map<String,Object> result = new HashMap<>();
        List<Map> list =  new ArrayList<>();
        for(WxMessage wm:wms){
            Map<String, Object> item = buildMessage(wm);
            list.add(item);
        }
        result.put("articles",list);
        return JSON.toJSONString(result);
    }

    /**
     * 上传图文消息，返回微信mediaId
     * @param httpBody
     * @return
     */
    public static String uploadMessage(String httpBody,String appid,String appSecret) throws LegoException {
        try {
            String res = HttpClientUtil.sendPostBody(Wx_Msg_PostNewsUrl.replace("ACCESS_TOKEN", WxUtil.getAccessTokenString(appid, appSecret)),httpBody);
            logger.info("----------上传群发图文消息的返回结果:{}----------"+res);
            String mediaId = "";
            try {
                JSONObject object = JSONObject.parseObject(res);
                mediaId = (String) object.get("media_id");
            } catch (JSONException e) {
                logger.error("上传群发图文消息发生错误{}"+res);
                throw new LegoException("上传微信图文失败", VendorExceptionCode.Lego_Error_WX_Upload_Error);
            }
            return mediaId;
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
    }

    /**
     * 微信群发 消息
     * @param mediaId
     */
    public static String sendToAll(String mediaId,String appid,String appSecret) throws LegoException {
        JSONObject msgObj = buildWxToAllObj(mediaId);
        String msg = msgObj.toJSONString();
        logger.info("----------群发的json数据:{}----------"+msg);
        String sentToAllResult = null;
        try {
            sentToAllResult = HttpClientUtil.sendPostBody(Wx_Msg_SendToAllUrl.replace("ACCESS_TOKEN", WxUtil.getAccessTokenString(appid, appSecret)), msg);
            logger.info("----------群发结果:{}----------"+sentToAllResult);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        return sentToAllResult;
    }

    /**
     * 指定群发的openId 列表
     * @param openIds
     * @param mediaId
     * @param appid
     * @param appSecret
     * @return
     */
    public static String sendToUserList(List<String> openIds,String mediaId,String appid,String appSecret) throws LegoException {
        JSONObject msgObj = buildWxToUserListObj(openIds,mediaId);
        String msg = msgObj.toJSONString();
        logger.info("----------指定openid群发的json数据:{}----------"+msg);
        String sentToUserListResult = null;
        try {
            sentToUserListResult = HttpClientUtil.sendPostBody(Wx_Msg_SendUrl.replace("ACCESS_TOKEN", WxUtil.getAccessTokenString(appid, appSecret)), msg);
            logger.info("----------指定openid群发结果:{}----------"+sentToUserListResult);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        return sentToUserListResult;
    }

    /**
     * 组装微信发送全部的json数据
     * 图文格式
     * {
         "filter":{
             "is_to_all":false,
             "tag_id":2
         },
         "mpnews":{
             "media_id":"123dsdajkasd231jhksad"
         },
         "msgtype":"mpnews",
         "send_ignore_reprint":0
         }
     * @param mediaId
     * @return
     */
    private static JSONObject buildWxToAllObj(String mediaId){
        JSONObject result = new JSONObject();

        JSONObject filter = new JSONObject();
        filter.put("is_to_all",true);
        result.put("filter",filter);

        JSONObject media = new JSONObject();
        media.put("media_id",mediaId);
        result.put("mpnews",media);
        result.put("msgtype","mpnews");
        result.put("send_ignore_reprint",0);
        return result;
    }

    /**
     * 组装微信发送置顶openId的json数据
     * 图文格式
     * {
         "touser":[
             "OPENID1",
             "OPENID2"
         ],
         "mpnews":{
            "media_id":"123dsdajkasd231jhksad"
         },
         "msgtype":"mpnews"，
         "send_ignore_reprint":0
         }
     * @param mediaId
     * @return
     */
    private static JSONObject buildWxToUserListObj(List<String> openIds,String mediaId){
        JSONObject result = new JSONObject();
        result.put("touser",openIds);
        JSONObject media = new JSONObject();
        media.put("media_id",mediaId);
        result.put("mpnews",media);
        result.put("msgtype","mpnews");
        result.put("send_ignore_reprint",0);
        return result;
    }




}
