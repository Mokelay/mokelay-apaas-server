package com.greatbee.core.bean.server;

import com.greatbee.base.bean.Identified;

/**
 * Lego Attribute
 * 用于在设置Lego的时候，需要额外配置的参数
 * TODO
 * <p/>
 * Author :CarlChen
 * Date:17/7/10
 */
public class LegoAttribute implements Identified {
    private Integer id;
    private String legoAlias;
    private String name;
    private String defaultValue;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLegoAlias() {
        return legoAlias;
    }

    public void setLegoAlias(String legoAlias) {
        this.legoAlias = legoAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
