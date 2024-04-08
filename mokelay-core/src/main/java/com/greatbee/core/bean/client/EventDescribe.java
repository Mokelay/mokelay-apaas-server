package com.greatbee.core.bean.client;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 事件描述
 * <p/>
 * Author: CarlChen
 * Date: 2017/8/14
 */
public class EventDescribe extends SimpleIND implements IND {
    //所属积木别名
    private String buildingBlockAlias;
    //事件名称
    private String eventName;

    public String getBuildingBlockAlias() {
        return buildingBlockAlias;
    }

    public void setBuildingBlockAlias(String buildingBlockAlias) {
        this.buildingBlockAlias = buildingBlockAlias;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
