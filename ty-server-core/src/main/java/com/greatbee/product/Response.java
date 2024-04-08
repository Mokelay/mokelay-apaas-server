package com.greatbee.product;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: CarlChen
 * Date: 2017/9/15
 */
public class Response {
    private boolean ok = true;
    private long code = 200;
    private String message;

    private Map<String, Object> data;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Add Data
     *
     * @param key
     * @param d
     */
    public void addData(String key, Object d) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, d);
    }
}
