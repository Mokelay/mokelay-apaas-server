package com.greatbee.core.lego.oss;

import com.aliyun.oss.OSSClient;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * CreateOssStorage
 *
 * 创建oss存储空间 lego
 * 同一用户创建的存储空间总数不能超过30个
 * 存储空间一旦创建成功，名称、所处地域、存储类型不能修改。
 * 单个存储空间的容量不限制。
 * @author xiaobc
 * @date 18/6/21
 */
@Component("createOssStorage")
public class CreateOssStorage extends OssBase{
    private static final Logger logger = Logger.getLogger(CreateOssStorage.class);

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String bucketName = input.getInputValue(Input_Key_Oss_Bucket_Name);
        if(StringUtil.isInvalid(bucketName)){
            throw new LegoException("OSS存储空间名称无效",ERROR_LEGO_OSS_Bucket_Name_Null);
        }
        OSSClient ossClient = this.createClient(input);
        //创建存储空间
        ossClient.createBucket(bucketName);

        this.closeClient(ossClient);

        //如果没有报错，将创建的存储空间返回，因为同一用户创建的存储空间总数不能超过30个
        output.setOutputValue(Input_Key_Oss_Bucket_Name,bucketName);
    }


}
