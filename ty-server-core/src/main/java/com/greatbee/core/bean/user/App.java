package com.greatbee.core.bean.user;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * User
 * <p/>
 * Author: CarlChen
 * Date: 2017/9/5
 */
public class App extends SimpleIND implements IND, CreateDateBean {
    private Integer id;
    private String name;
    private String description;
    private String alias;
    private Date createDate;
    private Boolean online;
    private String entrance;
    //巴斯别名
    private String buzzAlias;

    private String icon;
    private Integer createrId;
    private String createrName;
    private Integer owerId;
    private String owerName;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

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

    public Boolean isOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getBuzzAlias() {
        return buzzAlias;
    }

    public void setBuzzAlias(String buzzAlias) {
        this.buzzAlias = buzzAlias;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Integer createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Integer getOwerId() {
        return owerId;
    }

    public void setOwerId(Integer owerId) {
        this.owerId = owerId;
    }

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }
}
