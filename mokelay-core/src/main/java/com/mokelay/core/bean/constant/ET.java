package com.mokelay.core.bean.constant;

/**
 * Edit Type
 * <p/>
 * Author :CarlChen
 * Date:17/8/10
 */
public enum ET {
    Input("input"),
    Number("number");
    private String type;

    ET(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
