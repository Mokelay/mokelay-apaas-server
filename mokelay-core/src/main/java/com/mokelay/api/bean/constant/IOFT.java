package com.mokelay.api.bean.constant;

/**
 * Input & Output Field Type
 *
 * Author :CarlChen
 * Date:17/7/11
 */
public enum IOFT {
    Condition("condition"),
    Read("read"),
    Create("create"),
    Update("update"),
    Common("common"),
    Unstructured("unstructured"),
    Cache("cache"),
    Memory("memory");


    private String type;

    IOFT(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
