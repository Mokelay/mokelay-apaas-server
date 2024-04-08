package com.greatbee.core.bean.constant;

/**
 * FT(Field Type)
 * <p/>
 * 1. 存储字段
 * 2. 虚拟字段
 * <p/>
 * Created by CarlChen on 16/10/11.
 */
public enum FT {
    Storage("storage"),
    Visual("visual");
    private String type;

    FT(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
