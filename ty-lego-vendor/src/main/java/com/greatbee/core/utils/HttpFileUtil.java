package com.greatbee.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 * HttpFileUtil
 * 文件上传
 *
 * @author xiaobc
 * @date 18/9/10
 */
public class HttpFileUtil {

    /**
     * 文件流上传
     * @param url
     * @param filePath
     */
    public static JSONObject inputStreamUpload(String url,String filePath) {
        //创建HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //构建POST请求   请求地址请更换为自己的。
        //1)
        HttpPost post = new HttpPost(url);
        InputStream inputStream = null;
        try {
            //文件路径
            //2)
            String[] paths = filePath.split("/");
            String fileName = paths[paths.length-1];
            inputStream = new FileInputStream(filePath);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //第一个参数为 相当于 Form表单提交的file框的name值 第二个参数就是我们要发送的InputStream对象了
            //第三个参数是文件名
            //3)
            builder.addBinaryBody("file", inputStream, ContentType.create("multipart/form-data"), fileName);
            //4)构建请求参数 普通表单项
            StringBody stringBody = new StringBody("12",ContentType.MULTIPART_FORM_DATA);
            builder.addPart("id",stringBody);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            //发送请求
            HttpResponse response = client.execute(post);
            entity = response.getEntity();
            if (entity != null) {
                String body = null;
                body = EntityUtils.toString(entity, "UTF-8");
                System.out.println(body);
                return JSON.parseObject(body);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 文件上传
     * @param url
     * @param filePath
     */
    public static JSONObject fileUpload(String url,String filePath) {
        //构建HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //构建POST请求
        HttpPost httpPost = new HttpPost(url);
        InputStream inputStream = null;
        try {
            String[] paths = filePath.split("/");
            String fileName = paths[paths.length-1];
            //要上传的文件
            File file = null; file = new File(filePath);
            //构建文件体
            FileBody fileBody = new FileBody(file,ContentType.MULTIPART_FORM_DATA,fileName);
            StringBody stringBody = new StringBody("12",ContentType.MULTIPART_FORM_DATA);
            HttpEntity httpEntity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("file", fileBody)
                    .addPart("id",stringBody).build();
            httpPost.setEntity(httpEntity);
            //发送请求
            HttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            if(httpEntity != null){
                String body = null;
                body = EntityUtils.toString(httpEntity, "UTF-8");
                System.out.println(body);
                return JSON.parseObject(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        fileUpload("http://local.dev.rs.com:8080/config/ty_oss_upload","/Users/xiaobc/tmp/test.txt");
    }


}
