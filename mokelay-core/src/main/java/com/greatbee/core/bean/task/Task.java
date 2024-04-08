package com.greatbee.core.bean.task;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * 自定义任务
 * <p/>
 * Author: CarlChen
 * Date: 2017/11/30
 */
public class Task extends SimpleIND implements IND, AliasBean, CreateDateBean {
    private String alias;
    private String group;
    private String cron;
    private String status;  //normal  正常   和  paused  停用
    private Date createDate;

    private String apiAlias;

    public String getApiAlias() {
        return apiAlias;
    }

    public void setApiAlias(String apiAlias) {
        this.apiAlias = apiAlias;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
}
