package com.greatbee.core.lego.weidian.bean;

/**
 * Param
 *
 * @author xiaobc
 * @date 18/10/14
 */
public class Param {

    private String name;
    private String value;

    public Param(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
