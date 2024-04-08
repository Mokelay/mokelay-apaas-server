package com.greatbee.vendor.lego.wx;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.vendor.lego.wx.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * wxPayNotify
 *
 * 微信支付 回调  lego
 *
 * 返回微信支付后的回调参数
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("wxPayNotify")
public class WxPayNotify extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxPayNotify.class);

    private static final String Output_Key_WX_Pay_Out_Trade_No = "out_trade_no";//订单编号
    private static final String Output_Key_WX_Pay_Total_Fee = "total_fee";//支付金额
    private static final String Output_Key_WX_Pay_Transaction_Id = "transaction_id";//支付凭证 交易号
    private static final String Output_Key_WX_Pay_Trade_Status = "trade_status";//支付状态 成功:SUCCESS  失败：FAIL

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        HttpServletRequest req = input.getRequest();
        try {
            String inputLine;
            String notityXml = "";
            try {
                while ((inputLine = req.getReader().readLine()) != null) {
                    notityXml += inputLine;
                }
                req.getReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map wxRsult = WxUtil.parseXmlToList(notityXml);
            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            //商户订单号
            String out_trade_no = wxRsult.get("out_trade_no").toString();
            //获取金额
            String total_fee = wxRsult.get("total_fee").toString();
            //微信交易号
            String trade_no = wxRsult.get("transaction_id").toString();
            //交易状态
            String trade_status = wxRsult.get("result_code").toString();
            logger.info("[WxPayNotify][receive,out_trade_no=" + out_trade_no + ",trade_status=" + trade_status + ",trade_status=" + trade_status + ",total_fee=" + total_fee + "]");
            logger.info("wxpay  start wxpay  notify................................");

            //返回支付返回的参数   供下面lego 业务逻辑处理
            output.setOutputValue(Output_Key_WX_Pay_Out_Trade_No,out_trade_no);
            output.setOutputValue(Output_Key_WX_Pay_Total_Fee,total_fee);
            output.setOutputValue(Output_Key_WX_Pay_Transaction_Id,trade_no);
            output.setOutputValue(Output_Key_WX_Pay_Trade_Status,trade_status);
        } catch (Exception e) {
            logger.error("------------------wx pay notify error!----------------------");
            logger.error(e);
            e.printStackTrace();
        }
    }


}
