package com.greatbee.core.bean.client;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * 积木
 * <p/>
 * Author :CarlChen
 * Date:17/7/8
 */
public class BuildingBlock extends SimpleIND implements IND, AliasBean, CreateDateBean {
    private String alias;
    private Date createDate;
    private String icon;

    //支持的平台
    private String platform;
    //Tags
    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
