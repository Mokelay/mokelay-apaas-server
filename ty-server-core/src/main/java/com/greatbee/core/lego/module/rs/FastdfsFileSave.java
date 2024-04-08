package com.greatbee.core.lego.module.rs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fastdfs File Upload
 * <p>
 * 输入：
 * 输出
 * Author :Xiaobc
 * Date:17/7/28
 */
@Component("fastdfsFileSave")
public class FastdfsFileSave implements Lego, ExceptionCode {

    private static final Logger logger = Logger.getLogger(FastdfsFileSave.class);

    @Autowired
    private TYDriver tyDriver;
    private static final String Input_Key_File_Fast_Stream = "file";//上传的文件流
    private static final String Input_Key_AppName = "appName";//应用名称，默认TY

    private static final String Output_Key_File_Fast_Url = "fileUrl";//带水印的图片地址
    private static final String Output_Key_File_Fast_Url_No_Water_Mark = "noWaterMarkFileUrl";//不带水印的图片地址
    private static final String Output_Key_File_Fast_Width = "width";//图片宽度
    private static final String Output_Key_File_Fast_Height = "height";//图片高度
    private static final String Output_Key_File_Fast_Type = "fileType";//图片类型
    private static final String Output_Key_File_Fast_FileId = "fileId";//图片ID
    private static final String Output_Key_File_FileNo = "fileNo";//图片编码
    private static final String Output_Key_File_Fast_Original_Name = "originaName";//图片原文件名
    private static final String Output_Key_File_Fast_Size = "size";//图片大小
    private static final String Output_Key_Upload_File_Time = "uploadTime";//上传时间

    @Value("${fileUploadUrl}")
    private String fileUploadUrl;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        try {
            HttpServletRequest req = input.getRequest();
            Object objectValue = input.getInputObjectValue(Input_Key_File_Fast_Stream);
            String appName = input.getInputValue(Input_Key_AppName);
            if (objectValue == null) {
                throw new LegoException("没有需要上传的文件", ERROR_LEGO_FILE_EMPTY);
            }
            if (objectValue instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) objectValue;

                String originalName = file.getOriginalFilename();
                long fileSize = file.getSize();
                String fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String now = sdf.format(new Date());
                String contentType = file.getContentType();

                MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
                param.add("file", new InputStreamResource(file.getInputStream()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }

                    @Override
                    public long contentLength() throws IOException {
                        return file.getSize();
                    }
                });

                param.add("appName", StringUtil.isValid(appName)?appName:"ty");
                param.add("token", req.getSession().getId());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity entity = new HttpEntity(param, headers);

                String result = restTemplate.postForObject(fileUploadUrl + "/file/public/upload/e", entity, String.class);
                logger.info("result===>" + result);

                String url = "";
                int width = 0;
                int height = 0;
                long fileId = 0l;
                String fileNo = "";

                if (!StringUtils.isEmpty(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    if ("200".equals(jsonObject.getString("status"))) {
                        JSONObject valueObject = JSON.parseObject(jsonObject.getString("value"));
                        url = valueObject.getString("fileUrl");
                        if(valueObject.containsKey("width")){
                            width = valueObject.getInteger("width");
                            height = valueObject.getInteger("height");
                        }
                        fileId = valueObject.getLong("fileId");
                        fileNo = valueObject.getString("fileNo");
                    }
                }
                output.setOutputValue(Output_Key_Upload_File_Time, now);
                output.setOutputValue(Output_Key_File_Fast_Size, fileSize);
                output.setOutputValue(Output_Key_File_Fast_Original_Name, originalName);
                output.setOutputValue(Output_Key_File_Fast_Type, fileType);
                output.setOutputValue(Output_Key_File_Fast_Url, url);
                output.setOutputValue(Output_Key_File_Fast_Url_No_Water_Mark, url+"!");
                output.setOutputValue(Output_Key_File_Fast_Width, width);
                output.setOutputValue(Output_Key_File_Fast_Height, height);
                output.setOutputValue(Output_Key_File_Fast_FileId, fileId);
                output.setOutputValue(Output_Key_File_FileNo, fileNo);

                FileStorage fileStorage = new FileStorage();
                fileStorage.setContentType(contentType);
                fileStorage.setFileSize(fileSize);
                fileStorage.setOiAlias("");
                fileStorage.setOriginalName(originalName);
                fileStorage.setSerializeName(originalName);
                fileStorage.setFileType(fileType);
                fileStorage.setFileUrl(url+"!");
                fileStorage.setUploadType("fastdfs");
                tyDriver.getFileStorageManager().add(fileStorage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("上传文件失败", e, ERROR_LEGO_FILE_SAVE_FAIL);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
