package com.mokelay.vendor.lego.oss;

import com.aliyun.oss.OSSClient;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.core.lego.util.LegoUtil;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * OssBase
 *  对接阿里云 oss 基类
 *
 *  endpoint 参考阿里云oss https://help.aliyun.com/document_detail/31837.html?spm=a2c4g.11186623.2.1.VsZgWK
 *
 *
 * @author xiaobc
 * @date 18/6/22
 */
public abstract class OssBase implements ExceptionCode, Lego {

    private static final Logger logger = Logger.getLogger(OssBase.class);

    protected static final long ERROR_LEGO_OSS_Endpoint_Error = 300024L;
    protected static final long ERROR_LEGO_OSS_Access_Key_Error = 300025L;
    protected static final long ERROR_LEGO_OSS_Bucket_Name_Null = 300026L;

    protected static final String Input_Key_Oss_Endpoint = "endpoint";//oss  访问域名
    protected static final String Input_Key_Oss_Access_Key_Id = "accessKeyId";//访问密钥
    protected static final String Input_Key_Oss_Access_Key_Secret = "accessKeySecret";//访问密钥

    //OSS 范围内必须是全局唯一的，一旦创建之后无法修改名称。
    protected static final String Input_Key_Oss_Bucket_Name = "bucketName";//oss  存储空间名字
    //拼接上传或者查询，返回文件url，方便显示或者下载
    protected static final String Input_Key_File_Download_Url = "oss_download_url";

    /**
     * 创建OSS客户端
     * @param input
     * @return
     * @throws LegoException
     */
    protected OSSClient createClient(Input input) throws LegoException{
        String endpoint = input.getInputValue(Input_Key_Oss_Endpoint);
        String accessKeyId = input.getInputValue(Input_Key_Oss_Access_Key_Id);
        String accessKeySecret = input.getInputValue(Input_Key_Oss_Access_Key_Secret);
        if(StringUtil.isInvalid(endpoint)){
            throw new LegoException("OSS 访问域名丢失",ERROR_LEGO_OSS_Endpoint_Error);
        }
        if(StringUtil.isInvalid(accessKeyId)||StringUtil.isInvalid(accessKeySecret)){
            throw new LegoException("OSS 访问密钥丢失",ERROR_LEGO_OSS_Access_Key_Error);
        }
        return this.createClient(endpoint,accessKeyId,accessKeySecret);

    }
    /**
     * 创建ossClient
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    protected OSSClient createClient(String endpoint,String accessKeyId,String accessKeySecret){
        // region请按实际情况填写。参考https://help.aliyun.com/document_detail/31837.html?spm=a2c4g.11186623.2.1.VsZgWK
        String _endpoint = endpoint.toLowerCase().startsWith("http")?endpoint:"http://"+endpoint;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(_endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    /**
     * 关闭OSSClient
     * @param ossClient
     */
    protected void closeClient(OSSClient ossClient){
        if(ossClient!=null){
            ossClient.shutdown();
        }
    }


    /**
     * 构建tpl模板需要的请求参数
     * @param input
     * @return
     * @throws LegoException
     */
    protected Map buildTplParams(Input input) throws LegoException {
        java.util.List ifs = input.getInputFields();
        Map<String,Object> params = LegoUtil.buildTPLParams(input.getRequest(),null,null,input);
        Iterator result = ifs.iterator();
        while(result.hasNext()) {
            InputField _if = (InputField)result.next();
            params.put(_if.getFieldName(), _if.getFieldValue());
        }
        return params;
    }


}
