package com.greatbee.core.bean.view;

import com.greatbee.core.bean.client.AttributeDescribe;
import com.greatbee.core.bean.client.BuildingBlock;
import com.greatbee.core.bean.client.EventDescribe;
import com.greatbee.core.bean.client.MethodDescribe;

import java.util.List;

/**
 * Author: CarlChen
 * Date: 2017/8/14
 */
public class BuildingBlockView {
    private BuildingBlock buildingBlock;

    private List<AttributeDescribe> attributeDescribes;
    private List<MethodDescribe> methodDescribes;
    private List<EventDescribe> eventDescribes;

    public BuildingBlock getBuildingBlock() {
        return buildingBlock;
    }

    public void setBuildingBlock(BuildingBlock buildingBlock) {
        this.buildingBlock = buildingBlock;
    }

    public List<AttributeDescribe> getAttributeDescribes() {
        return attributeDescribes;
    }

    public void setAttributeDescribes(List<AttributeDescribe> attributeDescribes) {
        this.attributeDescribes = attributeDescribes;
    }

    public List<MethodDescribe> getMethodDescribes() {
        return methodDescribes;
    }

    public void setMethodDescribes(List<MethodDescribe> methodDescribes) {
        this.methodDescribes = methodDescribes;
    }

    public List<EventDescribe> getEventDescribes() {
        return eventDescribes;
    }

    public void setEventDescribes(List<EventDescribe> eventDescribes) {
        this.eventDescribes = eventDescribes;
    }
}
