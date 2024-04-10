package com.mokelay.core.bean.server;

import com.mokelay.base.bean.Identified;

/**
 * File Storage
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
public class FileStorage implements Identified {
    private Integer id;

    private String oiAlias;

    private String serializeName;
    private String originalName;
    private Long fileSize;
    private String fileType;
    private String contentType;

    private String fileUrl;//文件地址
    private String uploadType;//上传类型  oss(oss上传),fastdfs(fastdfs上传),server(服务器上传)

    @Override
    public Integer getId() {
        return id;
    }

    public String getOiAlias() {
        return oiAlias;
    }

    public String getSerializeName() {
        return serializeName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOiAlias(String oiAlias) {
        this.oiAlias = oiAlias;
    }

    public void setSerializeName(String serializeName) {
        this.serializeName = serializeName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }
}
