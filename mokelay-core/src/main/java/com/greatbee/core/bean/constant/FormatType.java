package com.greatbee.core.bean.constant;

/**
 * 格式类型 目前只有sessionread用到了
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/6
 */
public enum FormatType {
    JSON("json"),
    OBJECT("object");

    private String type;

    FormatType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
