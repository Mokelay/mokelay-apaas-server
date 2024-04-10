package com.mokelay.core.bean.server;

import com.mokelay.base.bean.AliasBean;
import com.mokelay.base.bean.CreateDateBean;
import com.mokelay.base.bean.IND;
import com.mokelay.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * API
 * <p/>
 * Author :CarlChen
 * Date:17/6/26
 */
public class API extends SimpleIND implements IND, AliasBean, CreateDateBean {
    public static final String VERSION_2 = "version_2";

    private String alias;
    private Date createDate;
    private Boolean enable;
    private String method;

    private String type;//类型别名（TY接口、测试接口还是其他接口）

    private String category;//接口类型 自定义接口和 配置接口 参考 APIT
    private String url;//接口地址  主要是配置自定义接口

    private String authType;//权限类型

    private String content;
    private String version;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
