package com.greatbee.core.bpm;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * Process Template
 * <p/>
 * 流程模板
 */
public class ProcessTemplate extends SimpleIND implements IND, CreateDateBean {
    //模板序列号
    private String serialNumber;

    //创建日期
    private Date createDate;

    //发起流程页面
    private String pageAlias;

    @Override
    public Date getCreateDate() {
        return null;
    }
}