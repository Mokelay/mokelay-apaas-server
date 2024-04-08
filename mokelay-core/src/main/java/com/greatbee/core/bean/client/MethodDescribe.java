package com.greatbee.core.bean.client;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 方法描述
 * <p/>
 * Author: CarlChen
 * Date: 2017/8/14
 */
public class MethodDescribe extends SimpleIND implements IND {
    //所属积木别名
    private String buildingBlockAlias;
    //方法名
    private String methodName;

    public String getBuildingBlockAlias() {
        return buildingBlockAlias;
    }

    public void setBuildingBlockAlias(String buildingBlockAlias) {
        this.buildingBlockAlias = buildingBlockAlias;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
