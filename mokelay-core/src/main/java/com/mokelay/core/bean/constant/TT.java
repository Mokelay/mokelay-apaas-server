package com.mokelay.core.bean.constant;

/**
 * Terminal Type
 * Created by CarlChen on 2016/11/11.
 */
public enum TT {
    Server("server"),
    Client("client");
    private String type;

    TT(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
