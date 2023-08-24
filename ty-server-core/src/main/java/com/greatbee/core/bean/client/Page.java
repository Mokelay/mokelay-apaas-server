package com.greatbee.core.bean.client;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * Page
 * <p/>
 * Author :CarlChen
 * Date:17/7/8
 */
public class Page extends SimpleIND implements IND, AliasBean, CreateDateBean {
    private String alias;
    private Date createDate;

    //布局
    private String layout;
    //布局类型：Block布局,Grid布局,自由式布局
    private String layoutType;

    //该页面是否是模板
    private boolean template;

    //该页面所属的模板
    private String templatePageAlias;

    //是否是定制化页面
    private boolean custom;
    //定制化文件
    private String customFile;
    //app别名
    private  String appAlias;
    //父id
    private Integer parentId;
    //是否有效
    private boolean enable;
    //类型  category,menu,sub
    private String type;
    //图标
    private String icon;
    //排序
    private Integer sort;
    //该页面下的数据源
    private String ds;
    //页面支持的平台
    private String platform;

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getCustomFile() {
        return customFile;
    }

    public void setCustomFile(String customFile) {
        this.customFile = customFile;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public String getTemplatePageAlias() {
        return templatePageAlias;
    }

    public void setTemplatePageAlias(String templatePageAlias) {
        this.templatePageAlias = templatePageAlias;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
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

    public String getAppAlias() {
        return appAlias;
    }

    public void setAppAlias(String appAlias) {
        this.appAlias = appAlias;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
