package com.mokelay.vendor.lego.oss;

import com.aliyun.oss.OSSClient;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OssDeleteFile
 * <p>
 * OSS删除文件
 * <p>
 * input:Input_Key_Oss_Endpoint,Input_Key_Oss_Access_Key_Id,Input_Key_Oss_Access_Key_Secret,Input_Key_Oss_Bucket_Name
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("ossDeleteFile")
public class OssDeleteFile extends OssBase {
    private static final Logger logger = Logger.getLogger(OssDeleteFile.class);

    private static final String Input_Key_File_Serialize_Name = "file_serialize_name";

    protected static final long ERROR_LEGO_OSS_Serialize_Name_Null = 300029L;

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String bucketName = input.getInputValue(Input_Key_Oss_Bucket_Name);
        String serializeName = input.getInputValue(Input_Key_File_Serialize_Name);
        if (StringUtil.isInvalid(bucketName)) {
            throw new LegoException("OSS存储空间名称无效", ERROR_LEGO_OSS_Bucket_Name_Null);
        }
        if (StringUtil.isInvalid(serializeName)) {
            throw new LegoException("OSS序列文件名称无效", ERROR_LEGO_OSS_Serialize_Name_Null);
        }

        //创建oss对象
        OSSClient ossClient = this.createClient(input);

        //oss删除文件
        ossClient.deleteObject(bucketName, serializeName);

        this.closeClient(ossClient);
    }


}
