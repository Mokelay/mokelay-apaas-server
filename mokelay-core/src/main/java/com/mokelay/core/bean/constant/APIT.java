package com.mokelay.core.bean.constant;

/**
 * API Type
 * 分配置接口和自定义接口
 * Author :CarlChen
 * Date:17/7/11
 */
public enum APIT {
    Config("config"),
    Custom("custom");
    private String type;

    APIT(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
