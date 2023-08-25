package com.greatbee.core.lego.wx;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.system.TYPPC;
import com.greatbee.core.lego.wx.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

/**
 * WxJssdkImgToInputStream
 *
 * 微信 js sdk 上传的 图片转到oss
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wxJssdkImgToInputStream")
public class WxJssdkImgToInputStream extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxJssdkImgToInputStream.class);

    private static final long Lego_Error_WX_Media_Id_Null = 300032L;
    private static final long Lego_Error_WX_Media_File_Error = 300033L;
    private static final long Lego_Error_WX_Media_File_Invalid = 300034L;

    private static final String Input_Key_WX_Media_Id = "media_id";//微信媒体文件 media_id

    private static final String Output_Key_Wx_Media_Stream = "wx_file_stream";//wx文件流

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String appId = input.getInputValue(Input_Key_Wx_Open_App_Id);
        String secret = input.getInputValue(Input_Key_WX_Open_Secret);
        if(StringUtil.isInvalid(appId)||StringUtil.isInvalid(secret)){
            throw new LegoException("微信参数缺失",ERROR_LEGO_WX_Params_Null);
        }
        String mediaId = input.getInputValue(Input_Key_WX_Media_Id);//获取微信媒体id
        if(StringUtil.isInvalid(mediaId)){
            throw new LegoException("微信媒体ID缺失",Lego_Error_WX_Media_Id_Null);
        }

        try {
            String accessToken = WxUtil.getAccessTokenString(appId, secret);

            String downloadUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+mediaId;

            URL url = new URL(downloadUrl);

            URLConnection uc = url.openConnection();
            String fileName = uc.getHeaderField(4);
            if(StringUtil.isInvalid(fileName)){
                throw new LegoException("媒体文件ID失效",Lego_Error_WX_Media_File_Invalid);
            }
            fileName = URLDecoder.decode(fileName.substring(fileName.indexOf("filename=")+9),"UTF-8");
            fileName = fileName.replaceAll("\"","");//去掉文件名前后的引号
            logger.info("[wxJssdkImgToInputStream] fileName="+fileName);
            BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
            //先将微信媒体文件存到本地
            String locaPath = WxJssdkImgToInputStream.class.getResource("/").getPath();
            String tmpPath = TYPPC.getTYProp("upload.temp.dir");
            if(StringUtil.isValid(tmpPath)){
                File tmpFile = new File(tmpPath);
                if(!tmpFile.getParentFile().exists()){
                    tmpFile.getParentFile().mkdirs();
                }
                locaPath = tmpPath;
            }
            String filePath = locaPath+fileName;
            logger.info("[wxJssdkImgToInputStream] filePath="+filePath);
            File file =  new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = null;
            buffer = new byte[2048];
            int length = in.read(buffer);
            while (length != -1) {
                out.write(buffer, 0, length);
                length = in.read(buffer);
            }
            in.close();
            out.close();

            output.setOutputValue(Output_Key_Wx_Media_Stream,new File(filePath));

        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException("WX签名失败",ERROR_LEFO_WX_Sign_Error);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new LegoException("下载微信媒体文件失败",Lego_Error_WX_Media_File_Error);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("下载微信媒体文件失败",Lego_Error_WX_Media_File_Error);
        }

    }





}
