package com.greatbee.core.bean.client;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 属性描述
 * <p/>
 * Author :CarlChen
 * Date:17/8/10
 */
public class AttributeDescribe extends SimpleIND implements IND {
    //所属积木别名
    private String buildingBlockAlias;
    //属性名
    private String attributeName;
    //属性数据类型
    private String dt;
    //上级属性
    private Integer parentId;
    //属性编辑类型
    private String et;
    //属性编辑类型数据
    private String etData;
    //编辑器属性
    private String etProps;

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getBuildingBlockAlias() {
        return buildingBlockAlias;
    }

    public void setBuildingBlockAlias(String buildingBlockAlias) {
        this.buildingBlockAlias = buildingBlockAlias;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getEtData() {
        return etData;
    }

    public void setEtData(String etData) {
        this.etData = etData;
    }

    public String getEtProps() {
        return etProps;
    }

    public void setEtProps(String etProps) {
        this.etProps = etProps;
    }
}
