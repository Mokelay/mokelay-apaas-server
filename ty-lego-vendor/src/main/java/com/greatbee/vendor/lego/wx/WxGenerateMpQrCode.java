package com.greatbee.vendor.lego.wx;

import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.vendor.lego.wx.util.WxMpUtil;
import com.greatbee.vendor.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * WxGenerateMpQrCode
 * <p>
 * 微信生成小程序二维码lego
 * <p>
 * 请求参数：scene={pageAlias}&page=pages/index/index&is_hyaline=false
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("wxGenerateMpQrCode")
public class WxGenerateMpQrCode extends WxAuth {

    private static final Logger logger = Logger.getLogger(WxGenerateMpQrCode.class);

    private static final String Input_Key_Wx_Mp_Qr_Code_Scene = "scene";//二维码跳转页面的请求参数
    private static final String Input_Key_Wx_Mp_Qr_Code_page = "page";//二维码的页面地址
    private static final String Input_Key_Wx_Mp_Qr_Code_is_hyaline = "hyaline";//二维码是否需要透明色，默认为false

    private static final String Input_Key_Wx_Mp_Appid = "appid";
    private static final String Input_Key_Wx_Mp_Appsecret = "appsecret";

    private static final String Output_Key_Wx_Mp_Qr_Code_File_Stream = "file_stream";//微信小程序二维码 文件流

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String scene = input.getInputValue(Input_Key_Wx_Mp_Qr_Code_Scene);//二维码参数
        String page = input.getInputValue(Input_Key_Wx_Mp_Qr_Code_page);//二维码页面
        boolean hyaline = DataUtil.getBoolean(input.getInputValue(Input_Key_Wx_Mp_Qr_Code_is_hyaline), false);//二维码是否透明

        String appId = input.getInputValue(Input_Key_Wx_Mp_Appid);
        String appSecret = input.getInputValue(Input_Key_Wx_Mp_Appsecret);

        if (StringUtil.isInvalid(appId)) {
            throw new LegoException("微信小程序appId必填", VendorExceptionCode.Lego_Error_WX_Mp_Appid_Null);
        }
        if (StringUtil.isInvalid(appSecret)) {
            throw new LegoException("微信小程序密钥必填", VendorExceptionCode.Lego_Error_WX_Mp_Appsecret_Null);
        }

        //获取微信小程序 二维码图片流
        InputStream is = WxMpUtil.getMiniQrCode(appId, appSecret, scene, page, hyaline);

        //返回给客户端输出
        FileStream fileStream = new FileStream(is);
        fileStream.setContentType("image/png");//图片
        output.setOutputValue(Output_Key_Wx_Mp_Qr_Code_File_Stream, fileStream);
    }

}
