package com.greatbee.core.lego.qiniu;

import com.google.gson.Gson;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.RandomGUIDUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.manager.TYDriver;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import net.sf.jmimemagic.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 *  七牛 音频转换 lego   主要是微信的amr格式音频转成mp3，web可以识别   也可以是七牛上传
 *
 * 实现逻辑是 拿到文件后 上传到七牛服务器，并且在七牛服务器转换成想要的格式，然后下载下来返回文件流,
 *
 * @author xiaobc
 * @date 18/7/16
 */
@Component("audioTransfer")
public class AudioTransfer implements ExceptionCode, Lego {

    private static final Logger logger = Logger.getLogger(AudioTransfer.class);

    private static final long ERROR_LEGO_QINIU_SECRET_INVALIDATE = 300050L;
    private static final long ERROR_LEGO_QINIU_BUCKET_INVALIDATE = 300051L;
    private static final long ERROR_LEGO_QINIU_QUEUE_INVALIDATE = 300052L;
    private static final long ERROR_LEGO_QINIU_FILESTREAM_INVALIDATE = 300053L;
    private static final long ERROR_LEGO_QINIU_PERSISTENT_INVALIDATE = 300054L;

    private static final String Input_Key_Qiniu_Zone = "zone";//服务器区域 zone  华东/华北/华南/北美
    private static final String Input_Key_Qiniu_Key = "key";//七牛公钥
    private static final String Input_Key_Qiniu_Secret = "secret";//七牛密钥
    private static final String Input_Key_Qiniu_Bucket = "bucket";//七牛存储空间
    private static final String Input_Key_Qiniu_Proper_Queue = "properQueue";//七牛专有队列

    private static final String Input_Key_Is_Save_Storage = "saveStorage";//是否保存到storage表

    private static final String Input_Key_Qiniu_Target_Type = "targetType";//需要转换成什么类型的文件

    private static final String Input_Key_Qiniu_Persistent_Url = "persistentUrl";//七牛处理完毕之后的回调接口

    //拼接上传或者查询，返回文件url，方便显示或者下载
    private static final String Input_Key_File_Download_Url = "qiniu_download_url";//七牛的下载域名 + xxx.mp3

    private static final String Input_Key_File_Oss_Download_Url = "oss_download_url";//oss下载域名 + xxx.mp3

    private static final String Input_Key_Qiniu_Format_String = "formatString";//七牛处理规格


    private static final String Input_Key_File_Stream = "file_stream";//文件流

    private static final String Output_Key_Target_File_Name = "fileName";//目标文件名
    private static final String Output_Key_Target_File_Path = "filePath";//目标文件地址 ,如果是图片，返回压缩后的图片地址   七牛地址

    private static final String Output_Key_Target_Oss_File_Path = "ossfilePath";//oss目标下载文件

    private static final String Output_Key_Target_Video_Thumbnail = "thumbnail";//视频缩略图地址

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String zone = input.getInputValue(Input_Key_Qiniu_Zone);//默认华南
        String accessKey = input.getInputValue(Input_Key_Qiniu_Key);//七牛公钥
        String secretKey = input.getInputValue(Input_Key_Qiniu_Secret);//七牛密钥
        String bucket = input.getInputValue(Input_Key_Qiniu_Bucket);//存储空间
        String properQueue = input.getInputValue(Input_Key_Qiniu_Proper_Queue);//七牛专有队列  上传用  数据处理队列名称
        String targetType = input.getInputValue(Input_Key_Qiniu_Target_Type);//需要转换的文件格式 默认是mp3
        String saveStorage = input.getInputValue(Input_Key_Is_Save_Storage);//是否保存到storage表中
        String persistentUrl = input.getInputValue(Input_Key_Qiniu_Persistent_Url);

        String formatString = input.getInputValue(Input_Key_Qiniu_Format_String);

        if(StringUtil.isInvalid(accessKey)||StringUtil.isInvalid(secretKey)){
            throw new LegoException("密钥、公钥配置无效",ERROR_LEGO_QINIU_SECRET_INVALIDATE);
        }
        if(StringUtil.isInvalid(bucket)){
            throw new LegoException("存储空间配置无效",ERROR_LEGO_QINIU_BUCKET_INVALIDATE);
        }
        if(StringUtil.isInvalid(properQueue)){
            throw new LegoException("私有队列无效",ERROR_LEGO_QINIU_QUEUE_INVALIDATE);
        }

        Object objectValue = input.getInputObjectValue(Input_Key_File_Stream);
        if (objectValue == null) {
            throw new LegoException("没有需要转换的文件", ERROR_LEGO_QINIU_FILESTREAM_INVALIDATE);
        }

        //构建下载地址
        String downloadUrl = input.getInputValue(Input_Key_File_Download_Url);
        Map params = _buildTplParams(input);
        String url = LegoUtil.transferInputValue(downloadUrl, params);//附带参数可能需要模板
        String ossDownloadUrl = input.getInputValue(Input_Key_File_Oss_Download_Url);
        String ossUrl = "";
        if(StringUtil.isValid(ossDownloadUrl)){
            ossUrl = LegoUtil.transferInputValue(ossDownloadUrl, params);//附带参数可能需要模板
        }

        try {
            InputStream is = null;
            String originalName = "";
            long fileSize = 0;
            String fileType = "";
            String serializeName = "";
            String contentType = "";

            String uuid = RandomGUIDUtil.getRawGUID();
            if(objectValue instanceof File){
                File file = (File) objectValue;
                is = new FileInputStream(file);
                originalName = file.getName();
                fileSize = file.length();
                fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                serializeName = uuid+"."+fileType;

                Magic parser = new Magic() ;
                MagicMatch match = parser.getMagicMatch(file,false);
                contentType = match.getMimeType();

            }else if(objectValue instanceof MultipartFile){
                MultipartFile file = (MultipartFile) objectValue;
                is = file.getInputStream();
                originalName = file.getOriginalFilename();
                fileSize = file.getSize();
                fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                serializeName = uuid+"."+fileType;
                contentType = file.getContentType();
            }else{
                throw new LegoException("没有需要转换的文件", ERROR_LEGO_QINIU_FILESTREAM_INVALIDATE);
            }

            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(this._getZone(zone));

            //生成上传凭证，然后准备上传
            UploadManager uploadManager = new UploadManager(cfg);

            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = uuid+"."+(StringUtil.isValid(targetType)?targetType:fileType);

            Auth auth = Auth.create(accessKey, secretKey);

            String upToken = "";

            //需要转码类型
            if(StringUtil.isValid(targetType)){
                //创建 数据处理逻辑
                if(StringUtil.isInvalid(persistentUrl)){
                    //没有回调，抛错
                    throw new LegoException("七牛数据处理回调无效",ERROR_LEGO_QINIU_PERSISTENT_INVALIDATE);
                }
                StringMap putPolicy = new StringMap();
                String saveAMREntry = String.format("%s:%s", bucket,key);
                String avthumbMp3Fop = String.format("avthumb/%s%s|saveas/%s", targetType,formatString, UrlSafeBase64.encodeToString(saveAMREntry));
                //将多个数据处理指令拼接起来
                String persistentOpfs = StringUtils.join(new String[]{
                        avthumbMp3Fop
                }, ";");
                if("mp4".equalsIgnoreCase(targetType)||"mov".equalsIgnoreCase(targetType)||"flv".equalsIgnoreCase(targetType)||"avi".equalsIgnoreCase(targetType)){
                    //如果目标转码是 视频格式，就生成一张缩略图
                    String saveJpgEntry = String.format("%s:%s.jpg", bucket,uuid);
                    String vframeJpgFop = String.format("vframe/jpg/offset/1|saveas/%s", UrlSafeBase64.encodeToString(saveJpgEntry));
                    persistentOpfs = StringUtils.join(new String[]{
                            avthumbMp3Fop,vframeJpgFop
                    }, ";");
                }

                putPolicy.put("persistentOps", persistentOpfs);
                //数据处理队列名称，必填
                putPolicy.put("persistentPipeline", properQueue);
                //数据处理完成结果通知地址
                putPolicy.put("persistentNotifyUrl", persistentUrl);

                long expireSeconds = 3600;
                upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                logger.info("[AudioTransfer] upToken="+upToken);
            }else{
                //不需要转码
                upToken = auth.uploadToken(bucket);
                logger.info("[AudioTransfer] upToken2="+upToken);
            }

            Response response = uploadManager.put(is,key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("key:"+putRet.key);
            logger.info("hash:" + putRet.hash);

            // 返回可以直接访问的文件地址 和文件名
            output.setOutputValue(Output_Key_Target_File_Name,putRet.key);
            String filePath = "";
            if("JPEG".equalsIgnoreCase(fileType)||"PNG".equalsIgnoreCase(fileType)||"JPG".equalsIgnoreCase(fileType)){
                //如果是图片格式文件，返回压缩后的图片地址地址  文件地址后加上 ?imageslim   七牛只支持这两种文件格式瘦身
                filePath = url+"/"+putRet.key+"?imageslim";
            }else{
                filePath = url+"/"+putRet.key;
            }
            output.setOutputValue(Output_Key_Target_File_Path,filePath);
            output.setOutputValue(Output_Key_Target_Video_Thumbnail,url+"/"+uuid+".jpg");//缩略图默认是jpg

            output.setOutputValue(Output_Key_Target_Oss_File_Path,ossUrl+putRet.key);

            if(StringUtil.isValid(saveStorage)){
                //如果saveStorage 为有值，就保存到storage表， 用于单独使用七牛上传
                FileStorage fileStorage = new FileStorage();
                fileStorage.setContentType(contentType);
                fileStorage.setFileSize(Long.valueOf(fileSize));
                fileStorage.setOiAlias(bucket);//oiAlias 存 bucketName  七牛的 bucket
                fileStorage.setOriginalName(originalName);
                fileStorage.setSerializeName(serializeName);
                fileStorage.setFileType(fileType);
                fileStorage.setFileUrl(filePath);
                fileStorage.setUploadType("qiniu");
                try {
                    this.tyDriver.getFileStorageManager().add(fileStorage);
                } catch (DBException e) {
                    e.printStackTrace();
                    throw new LegoException(e.getMessage(), e.getCode());
                }
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            logger.error(r.toString());
            try {
                System.err.println(r.bodyString());
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MagicMatchNotFoundException e) {
            e.printStackTrace();
        } catch (MagicException e) {
            e.printStackTrace();
        } catch (MagicParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 华东z0/华北z1/华南z2/北美na0
     * @param zone
     * @return
     */
    private Zone _getZone(String zone){
        if("z0".equalsIgnoreCase(zone)){
            return Zone.zone0();
        }else if("z1".equalsIgnoreCase(zone)){
            return Zone.zone1();
        }else if("na0".equalsIgnoreCase(zone)){
            return Zone.zoneNa0();
        }else {
            //默认华南
            return Zone.zone2();
        }
    }

    /**
     * 构建tpl模板需要的请求参数
     * @param input
     * @return
     * @throws LegoException
     */
    private Map _buildTplParams(Input input) throws LegoException {
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
