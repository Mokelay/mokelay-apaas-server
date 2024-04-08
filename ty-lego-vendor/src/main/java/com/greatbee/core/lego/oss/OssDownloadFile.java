package com.greatbee.core.lego.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.utils.VendorUtil;
import com.greatbee.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ossDownloadFile
 * <p>
 * OSS下载文件
 * <p>
 * input:Input_Key_Oss_Endpoint,Input_Key_Oss_Access_Key_Id,Input_Key_Oss_Access_Key_Secret,Input_Key_Oss_Bucket_Name
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("ossDownloadFile")
public class OssDownloadFile extends OssBase {
    private static final Logger logger = Logger.getLogger(OssDownloadFile.class);

    private static final String Input_Key_File_Serialize_Name = "file_serialize_name";
    private static final String Input_Key_File_Name = "file_name";

    private static final String Input_Key_Use_File_Storage = "useFileStorage";//是否使用TY库的fileStorage

    private static final String Output_Key_File_Stream = "file_stream";

    protected static final long ERROR_LEGO_OSS_Serialize_Name_Null = 300029L;


    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String bucketName = input.getInputValue(Input_Key_Oss_Bucket_Name);
        String serializeName = input.getInputValue(Input_Key_File_Serialize_Name);
        String fileName = input.getInputValue(Input_Key_File_Name);
        boolean useFileStorage = DataUtil.getBoolean(input.getInputValue(Input_Key_Use_File_Storage), false);
        if (StringUtil.isInvalid(bucketName)) {
            throw new LegoException("OSS存储空间名称无效", ERROR_LEGO_OSS_Bucket_Name_Null);
        }
        if(StringUtil.isInvalid(serializeName)){
            throw new LegoException("OSS序列文件名称无效", ERROR_LEGO_OSS_Serialize_Name_Null);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        String now = sdf.format(new Date());

        //创建oss对象
        OSSClient ossClient = this.createClient(input);

        //调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及其元信息（meta）。
        OSSObject ossObject = ossClient.getObject(bucketName, serializeName);
        //调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream content = ossObject.getObjectContent();

        try {
            FileStream fileStream = new FileStream(content);
            if(useFileStorage){
                FileStorage fileStorage = this.tyDriver.getFileStorageManager().getFileStorage(serializeName);
                fileStream.setContentType(fileStorage.getContentType());
                fileStream.setFileName(StringUtil.isValid(fileName) ? fileName + now + "." + fileStorage.getFileType() : fileStorage.getOriginalName());
            }else{
                String[] fileSplits = serializeName.split("\\.");
                String suffix = fileSplits[fileSplits.length - 1];
                fileStream.setContentType(VendorUtil.findContentTypeBySuffix(suffix));
                fileStream.setFileName(StringUtil.isValid(fileName) ? fileName + now + "." + suffix: serializeName);
            }
            output.setOutputValue(Output_Key_File_Stream, fileStream);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }


}
