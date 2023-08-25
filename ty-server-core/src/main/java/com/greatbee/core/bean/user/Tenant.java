package com.greatbee.core.bean.user;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.Identified;

import java.util.Date;

/**
 * 租户
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/19
 */
public class Tenant implements Identified, CreateDateBean {
    private Integer id;
    private Date createDate;

    //序列号
    private String serialNumber;
    //公司名
    private String companyName;

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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
