package com.greatbee.core.lego.wx;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.wx.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * wxPrePay
 *
 * 微信支付 签名 lego
 *
 * 根据订单信息 生成js调用微信支付 需要的参数，包括签名、时间戳等
 *
 * lego需要传递的参数：
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
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("wxPrePay")
public class WxPrePay extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxPrePay.class);

    private static final long ERROR_LEGO_WX_PAY_Order_Number_Error = 300110L;
    private static final long ERROR_LEGO_WX_PAY_User_Openid_Error = 300111L;
    private static final long ERROR_LEGO_WX_PAY_Appid_Error = 300115L;
    private static final long ERROR_LEGO_WX_PAY_Mch_Id_Error = 300116L;
    private static final long ERROR_LEGO_WX_PAY_Notify_Url_Error = 300117L;
    private static final long ERROR_LEGO_WX_PAY_Total_Fee_Error = 300118L;
    private static final long ERROR_LEGO_WX_PAY_Api_Secret_Error = 300119L;

    private static final String Input_Key_Wx_Appid = "appid";//微信appId
    private static final String Input_Key_Order_Serial_Number = "serialNumber";//订单编号
    private static final String Input_Key_Wx_Mch_Id = "mch_id";//商户id
    private static final String Input_Key_Wx_Notify_Url = "notify_url";//回调通知地址
    private static final String Input_Key_User_Wx_Openid = "openId";//用户的oepnId
    private static final String Input_Key_Wx_Total_Fee = "total_fee";//总金额
    private static final String Input_Key_Wx_Api_Secret = "wx_api_secret";//微信公众号密钥
    private static final String Input_Key_Wx_Order_Description = "body";//商品描述


    private static final String Output_Key_WX_Pay_Param = "wx_pay_param";//js sdk 需要的加密参数

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String serialNumber = input.getInputValue(Input_Key_Order_Serial_Number);
        String openId = input.getInputValue(Input_Key_User_Wx_Openid);
        String appid = input.getInputValue(Input_Key_Wx_Appid);
        String mchId = input.getInputValue(Input_Key_Wx_Mch_Id);
        String notifyUrl = input.getInputValue(Input_Key_Wx_Notify_Url);
        String totalFee = input.getInputValue(Input_Key_Wx_Total_Fee);
        String apiSecret = input.getInputValue(Input_Key_Wx_Api_Secret);
        String body = input.getInputValue(Input_Key_Wx_Order_Description);//订单详情

        if(StringUtil.isInvalid(serialNumber)){
            throw new LegoException("订单编号必填",ERROR_LEGO_WX_PAY_Order_Number_Error);
        }
        if(StringUtil.isInvalid(openId)){
            throw new LegoException("未获取用户微信openId",ERROR_LEGO_WX_PAY_User_Openid_Error);
        }
        if(StringUtil.isInvalid(appid)){
            throw new LegoException("微信appId必填",ERROR_LEGO_WX_PAY_Appid_Error);
        }
        if(StringUtil.isInvalid(mchId)){
            throw new LegoException("微信商户ID必填",ERROR_LEGO_WX_PAY_Mch_Id_Error);
        }
        if(StringUtil.isInvalid(notifyUrl)){
            throw new LegoException("微信支付回调地址必填",ERROR_LEGO_WX_PAY_Notify_Url_Error);
        }
        if(StringUtil.isInvalid(totalFee)){
            throw new LegoException("微信支付金额必填",ERROR_LEGO_WX_PAY_Total_Fee_Error);
        }
        if(StringUtil.isInvalid(apiSecret)){
            throw new LegoException("微信公众号密钥必填",ERROR_LEGO_WX_PAY_Api_Secret_Error);
        }

        /**
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
         */
        Map<String,Object> map = new HashMap<>();
        map.put("appid",appid);
        map.put("serialNumber",serialNumber);
        map.put("mch_id",mchId);
        map.put("notify_url",notifyUrl);
        map.put("openid",openId);
        map.put("total_fee",totalFee);
        map.put("trade_type","JSAPI");
        map.put("wx_api_secret",apiSecret);
        map.put("body",body);
        JSONObject resJson = null;
        try {
            resJson = WxUtil.weixinOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回js调起支付需要的参数
        output.setOutputValue(Output_Key_WX_Pay_Param,resJson);

    }



}
