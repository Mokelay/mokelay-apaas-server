package com.greatbee.core.bean.client;

import com.greatbee.base.bean.Identified;

/**
 * Page BuildingBlock
 * <p/>
 * Author :CarlChen
 * Date:17/8/10
 */
public class PageBuildingBlock implements Identified {
    private Integer id;
    private String pageAlias;
    private String buildingBlockAlias;

    private String attributes;//pbb的属性值 大json

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPageAlias() {
        return pageAlias;
    }

    public void setPageAlias(String pageAlias) {
        this.pageAlias = pageAlias;
    }

    public String getBuildingBlockAlias() {
        return buildingBlockAlias;
    }

    public void setBuildingBlockAlias(String buildingBlockAlias) {
        this.buildingBlockAlias = buildingBlockAlias;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
