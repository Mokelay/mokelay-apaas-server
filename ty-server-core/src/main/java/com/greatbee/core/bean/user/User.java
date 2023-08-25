package com.greatbee.core.bean.user;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.Identified;

import java.util.Date;

/**
 * User
 * <p/>
 * Author: CarlChen
 * Date: 2017/9/5
 */
public class User implements Identified, CreateDateBean {
    private int id;
    private Date createDate;
    private String xingMing;
    private String userName;
    private String password;

    private String alias;
    private String tenantSerialNumber;

    public String getTenantSerialNumber() {
        return tenantSerialNumber;
    }

    public void setTenantSerialNumber(String tenantSerialNumber) {
        this.tenantSerialNumber = tenantSerialNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Integer getId() {
        return id;
    }

    public String getXingMing() {
        return xingMing;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setXingMing(String xingMing) {
        this.xingMing = xingMing;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
