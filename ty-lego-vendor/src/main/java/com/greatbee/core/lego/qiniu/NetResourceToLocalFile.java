package com.greatbee.core.lego.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.utils.VendorUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 网络资源转存为本地临时文件 eg: 七牛公有文件转成file对象  方便后面上传到oss
 *
 * @author xiaobc
 * @date 18/7/16
 */
@Component("netResourceToLocalFile")
public class NetResourceToLocalFile implements ExceptionCode, Lego {

    private static final Logger logger = Logger.getLogger(NetResourceToLocalFile.class);

    private static final long ERROR_LEGO_NET_RESOURCE_INVALIDATE = 300054L;


    private static final String Input_Key_Net_Resource_Url = "resourceUrl";
    private static final String Input_Key_Net_Resource_FileName = "fileName";

    private static final String Output_Key_Net_Resource_Stream = "net_file_stream";//网络文件流

    private static final String Output_Key_Net_Resource_Content_Type = "contentType";//资源contentType

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String resourceUrl = input.getInputValue(Input_Key_Net_Resource_Url);
        String fileName = input.getInputValue(Input_Key_Net_Resource_FileName);
        if(StringUtil.isInvalid(resourceUrl)){
            throw new LegoException("资源地址无效",ERROR_LEGO_NET_RESOURCE_INVALIDATE);
        }

        JSONObject obj = VendorUtil.getLocalFileFromNetFile(resourceUrl, fileName);

        String filePath = "";
        String contentType = "";
        if(obj!=null && obj.containsKey("filePath")){
            filePath = obj.getString("filePath");
            contentType = obj.getString("contentType");
        }
        output.setOutputValue(Output_Key_Net_Resource_Stream,new File(filePath));
        output.setOutputValue(Output_Key_Net_Resource_Content_Type,contentType);
    }

}
