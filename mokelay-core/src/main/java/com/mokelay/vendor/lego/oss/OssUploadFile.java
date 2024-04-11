package com.mokelay.vendor.lego.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.RandomGUIDUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.core.bean.server.FileStorage;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.util.LegoUtil;
import com.mokelay.core.lego.util.StorageUtil;
import com.mokelay.core.manager.TYDriver;
import net.sf.jmimemagic.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * ossUploadFile
 * <p>
 * OSS上传文件
 * <p>
 * input:Input_Key_Oss_Endpoint,Input_Key_Oss_Access_Key_Id,Input_Key_Oss_Access_Key_Secret,Input_Key_Oss_Bucket_Name
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("ossUploadFile")
public class OssUploadFile extends OssBase {
    private static final Logger logger = Logger.getLogger(OssUploadFile.class);

    private static final String Input_Key_File_Stream = "file_stream";
    private static final String Input_Key_File_Content_Type = "contentType";//文件类型

    private static final String Input_Key_File_Serialize_Name = "file_serialize_name";//序列化的文件名 eg:xxx.jpg


    private static final long Lego_Error_No_File_Need_To_Upload = 300027L;
    private static final long Lego_Error_File_Stream_Error = 300028L;

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String contentType = input.getInputValue(Input_Key_File_Content_Type);
        String targetName = input.getInputValue(Input_Key_File_Serialize_Name);//目标文件名
        String bucketName = input.getInputValue(Input_Key_Oss_Bucket_Name);
        if (StringUtil.isInvalid(bucketName)) {
            throw new LegoException("OSS存储空间名称无效", ERROR_LEGO_OSS_Bucket_Name_Null);
        }
        Object objectValue = input.getInputObjectValue(Input_Key_File_Stream);
        if (objectValue == null) {
            throw new LegoException("没有需要上传的文件", Lego_Error_No_File_Need_To_Upload);
        }
        //构建下载地址
        String downloadUrl = input.getInputValue(Input_Key_File_Download_Url);
        Map params = buildTplParams(input);
        String url = LegoUtil.transferInputValue(downloadUrl, params);//附带参数可能需要模板

        //上传文件
        if (objectValue instanceof MultipartFile || objectValue instanceof File) {
            //创建oss对象
            OSSClient ossClient = this.createClient(input);

            String originalName = "";
            long fileSize = 0;
            String fileType = "";
            String serializeName = "";
            try {
                if (objectValue instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) objectValue;
                    originalName = file.getOriginalFilename();
                    fileSize = file.getSize();
                    fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                    if(StringUtil.isInvalid(targetName)){
                        serializeName = RandomGUIDUtil.getRawGUID() + "." + fileType;
                    }else{
                        serializeName = targetName;
                    }

                    if(StringUtil.isInvalid(contentType)){
                        contentType = file.getContentType();
                    }
                    //上传文件
                    ossClient.putObject(new PutObjectRequest(bucketName, serializeName, file.getInputStream()));
                }else{
                    File file = (File) objectValue;
                    originalName = file.getName();
                    fileSize = file.length();
                    fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                    if(StringUtil.isInvalid(targetName)){
                        serializeName = RandomGUIDUtil.getRawGUID() + "." + fileType;
                    }else{
                        serializeName = targetName;
                    }
                    if(StringUtil.isInvalid(contentType)){
                        Magic parser = new Magic() ;
                        MagicMatch match = parser.getMagicMatch(file,false);
                        contentType = match.getMimeType();
                    }
                    //上传文件
                    ossClient.putObject(new PutObjectRequest(bucketName, serializeName, file));
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new LegoException("文件流错误", Lego_Error_File_Stream_Error);
            } catch (MagicMatchNotFoundException e) {
                e.printStackTrace();
                throw new LegoException("文件流错误", Lego_Error_File_Stream_Error);
            } catch (MagicException e) {
                e.printStackTrace();
                throw new LegoException("文件流错误", Lego_Error_File_Stream_Error);
            } catch (MagicParseException e) {
                e.printStackTrace();
                throw new LegoException("文件流错误", Lego_Error_File_Stream_Error);
            }

            FileStorage fileStorage = new FileStorage();
            fileStorage.setContentType(contentType);
            fileStorage.setFileSize(Long.valueOf(fileSize));
            fileStorage.setOiAlias(bucketName);//oiAlias 存 bucketName
            fileStorage.setOriginalName(originalName);
            fileStorage.setSerializeName(serializeName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileUrl(url + serializeName);
            fileStorage.setUploadType("oss");
            try {
                StorageUtil.addFileStorage(fileStorage,output,tyDriver);
            } catch (DBException e) {
                e.printStackTrace();
                throw new LegoException(e.getMessage(), e.getCode());
            } finally {
                this.closeClient(ossClient);
            }
            this.closeClient(ossClient);
        }

    }


}
