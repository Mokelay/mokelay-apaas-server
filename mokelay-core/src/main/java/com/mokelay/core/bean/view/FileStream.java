package com.mokelay.core.bean.view;

import com.aliyun.oss.OSSClient;
import com.mokelay.base.util.StringUtil;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 存储
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
public class FileStream {
    private InputStream inputStream;
    private String contentType;
    private String fileName;
    private OSSClient ossClient;//oss client对象，在下载后，关闭client

    public FileStream(File file) throws FileNotFoundException {
        inputStream = new FileInputStream(file);
    }

    public FileStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public OSSClient getOssClient() {
        return ossClient;
    }

    public void setOssClient(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    /**
     * Download
     *
     * @param response response
     */
    public void download(HttpServletResponse response) {
        OutputStream out = null;
        try {
            response.reset();
            if (StringUtil.isValid(contentType)) {
                response.setContentType(contentType);
            }
            if (StringUtil.isValid(fileName)) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            }
            out = response.getOutputStream();
            byte[] bts = IOUtils.toByteArray(inputStream);
            out.write(bts);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
            if(ossClient!=null){
                ossClient.shutdown();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
