package com.greatbee.core.bean.client;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 数据描述
 * <p/>
 * Author: CarlChen
 * Date: 2017/9/17
 */
public class DataDescribe extends SimpleIND implements IND {
    //所属积木别名
    private String buildingBlockAlias;

    //变量名
    private String variableName;
    //数据类型
    private String dt;
    //上级属性
    private Integer parentId;

    public String getBuildingBlockAlias() {
        return buildingBlockAlias;
    }

    public void setBuildingBlockAlias(String buildingBlockAlias) {
        this.buildingBlockAlias = buildingBlockAlias;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
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
}
