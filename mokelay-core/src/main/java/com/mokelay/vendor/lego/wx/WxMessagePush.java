package com.mokelay.vendor.lego.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.vendor.lego.wx.model.WxMessage;
import com.mokelay.vendor.lego.wx.util.WxMsgUtil;
import com.mokelay.vendor.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * wxMessagePush
 *
 * 微信消息推送 lego
 * 支持置顶openid列表推送，也支持推送全部公众号用户
 * 如果openIds 不传及是推送的所有用户，传了就传openids的用户
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("wxMessagePush")
public class WxMessagePush extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxMessagePush.class);

    private static final long ERROR_LEGO_WX_PAY_Appid_Error = 300115L;
    private static final long ERROR_LEGO_WX_PAY_Api_Secret_Error = 300119L;

    private static final String Input_Key_Wx_Appid = "appid";//微信appId
    private static final String Input_Key_Wx_Api_Secret = "wx_api_secret";//微信公众号密钥
    private static final String Input_Key_Wx_Openid_List = "wx_open_id_list";//需要推送的用户oepnid 不传就是推送全部

    private static final String Input_Key_List_Param = "list_Param";//一次推送要发送的文章列表，json。最多只能8篇文章

    /**
     * 下面是非结构化数据
     */
    private static final String Input_Key_Wx_Msg_Image_Url = "imageUrl";//微信消息封面图
    private static final String Input_Key_Wx_Msg_Author = "author";//微信消息作者
    private static final String Input_Key_Wx_Msg_Title = "title";//微信消息标题
    private static final String Input_Key_Wx_Msg_Content_Source_Url = "contentSourceUrl";//微信消息点击阅读详情 的跳转地址
    private static final String Input_Key_Wx_Msg_Content = "content";//微信消息内容
    private static final String Input_Key_Wx_Msg_Show_Cover_Pic = "showCoverPic";//微信消息 显示封面图


    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String appid = input.getInputValue(Input_Key_Wx_Appid);
        String apiSecret = input.getInputValue(Input_Key_Wx_Api_Secret);
        //微信用户openId列表
        String openIds = input.getInputField(Input_Key_Wx_Openid_List).fieldValueToString();

        if(StringUtil.isInvalid(appid)){
            throw new LegoException("微信appId必填",ERROR_LEGO_WX_PAY_Appid_Error);
        }
        if(StringUtil.isInvalid(apiSecret)){
            throw new LegoException("微信公众号密钥必填",ERROR_LEGO_WX_PAY_Api_Secret_Error);
        }

        InputField imageUrl = input.getInputField(Input_Key_Wx_Msg_Image_Url);
        InputField author = input.getInputField(Input_Key_Wx_Msg_Author);
        InputField title = input.getInputField(Input_Key_Wx_Msg_Title);
        InputField contentSourceUrl = input.getInputField(Input_Key_Wx_Msg_Content_Source_Url);
        InputField content = input.getInputField(Input_Key_Wx_Msg_Content);
        InputField showCoverPic = input.getInputField(Input_Key_Wx_Msg_Show_Cover_Pic);

        InputField listParam = input.getInputField(Input_Key_List_Param);

        //微信推送消息列表
        List<WxMessage> list = new ArrayList<>();

        if(listParam.fieldValueToString()==null){
            //没有传 json 数据，直接读取非结构化数据的字段，表示单个消息推送
            WxMessage wm = new WxMessage();
            if(StringUtil.isInvalid(content.fieldValueToString())){
                //如果没有推送内容，直接结束
                throw new LegoException("请配置推送内容", VendorExceptionCode.Lego_Error_WX_Push_Error);
            }
            //封面图片
            if(StringUtil.isValid(imageUrl.fieldValueToString())){
                wm.setImageUrl(imageUrl.fieldValueToString());
            }
            //作者
            if(StringUtil.isValid(author.fieldValueToString())){
                wm.setAuthor(author.fieldValueToString());
            }
            //标题
            if(StringUtil.isValid(title.fieldValueToString())){
                wm.setTitle(title.fieldValueToString());
            }
            //点击详情跳转地址
            if(StringUtil.isValid(contentSourceUrl.fieldValueToString())){
                wm.setContentSourceUrl(contentSourceUrl.fieldValueToString());
            }
            //设置推送消息内容
            if(StringUtil.isValid(content.fieldValueToString())){
                wm.setContent(content.fieldValueToString());
            }
            //设置推送消息内容
            if(StringUtil.isValid(showCoverPic.fieldValueToString())){
                wm.setShowCoverPic(DataUtil.getInt(showCoverPic.getFieldValue(),1));
            }
            list.add(wm);
        }else {
            //读取json 数据，表示一次推送有单条或者多条数据,非结构化数据从json中获取数据
            JSONArray ja = null;
            try {
                ja = JSONArray.parseArray(listParam.fieldValueToString());
            } catch (Exception e) {
                e.printStackTrace();
                throw new LegoException("请求参数必须是有效的jsonString格式", 300003);
            }

            int countFlag = 8;
            for (int i = 0; i < ja.size(); i++) {
                if (countFlag <= 0) {
                    //微信消息最多只能发送8条消息
                    break;
                }
                WxMessage wm = new WxMessage();
                countFlag--;
                JSONObject jobj = ja.getJSONObject(i);

                //封面图片
                if (StringUtil.isInvalid(imageUrl.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(imageUrl.getAlias())) {
                        wm.setImageUrl(jobj.getString(imageUrl.getAlias()));
                    }
                } else {
                    wm.setImageUrl(imageUrl.fieldValueToString());
                }
                //作者
                if (StringUtil.isInvalid(author.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(author.getAlias())) {
                        wm.setAuthor(jobj.getString(author.getAlias()));
                    }
                } else {
                    wm.setAuthor(author.fieldValueToString());
                }
                //标题
                if (StringUtil.isInvalid(title.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(title.getAlias())) {
                        wm.setTitle(jobj.getString(title.getAlias()));
                    }
                } else {
                    wm.setTitle(title.fieldValueToString());
                }
                //点击详情跳转地址
                if (StringUtil.isInvalid(contentSourceUrl.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(contentSourceUrl.getAlias())) {
                        wm.setContentSourceUrl(jobj.getString(contentSourceUrl.getAlias()));
                    }
                } else {
                    wm.setContentSourceUrl(contentSourceUrl.fieldValueToString());
                }
                //设置推送消息内容
                if (StringUtil.isInvalid(content.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(content.getAlias())) {
                        wm.setContent(jobj.getString(content.getAlias()));
                    }
                } else {
                    wm.setContent(content.fieldValueToString());
                }
                //设置推送消息内容
                if (StringUtil.isInvalid(showCoverPic.fieldValueToString())) {
                    //没有value值，设置json中的值    --此处用alias 来取值--
                    if (jobj.containsKey(showCoverPic.getAlias())) {
                        wm.setShowCoverPic(jobj.getInteger(showCoverPic.getAlias()));
                    }
                } else {
                    wm.setShowCoverPic(DataUtil.getInt(showCoverPic.getFieldValue(), 1));
                }
                //添加到list
                list.add(wm);
            }
        }
        /**
         *  微信消息处理
         */
        //第一步 上传每个消息的封面图，网络地址转换成微信mediaId
        for(WxMessage wm:list){
            WxMsgUtil.setCoverImage(wm,appid,apiSecret);
        }
        //第二步 将图文 上传到微信  返回微信的mediaID
        String msg = WxMsgUtil.buildFullMessage(list);
        String mediaId = WxMsgUtil.uploadMessage(msg,appid,apiSecret);
        //第三部 发送 (分根据openId发送 和 群发(关注该公众号的用户))
        String result = null;
        if(StringUtil.isValid(openIds)){
            //根据openid发送
            List<String> openIdList = Arrays.asList(openIds.split(","));
            result = WxMsgUtil.sendToUserList(openIdList,mediaId,appid,apiSecret);
        }else{
            //推送全部用户
            result = WxMsgUtil.sendToAll(mediaId,appid,apiSecret);
        }
        logger.info("result="+result);
    }

}
