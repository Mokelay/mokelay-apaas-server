package com.greatbee.core.bpm;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * 业务流程
 */
public class BusinessProcess extends SimpleIND implements IND, CreateDateBean {
    //流程序列号
    private String serialNumber;

    //发起人
    private String originator;
    private String originatorId;

    //创建日期
    private Date createDate;

    //发起流程页面
    private String pageAlias;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }
}