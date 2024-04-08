package com.greatbee.base.bean;

import com.greatbee.base.util.DataUtil;

import java.util.LinkedHashMap;

/**
 * 数据
 *
 * Created by CarlChen on 2016/11/12.
 */
public class Data extends LinkedHashMap<String,Object> {



//    private Map<String, Object> data;

//    public Data() {
//    }
//    public Data(Map<String, Object> data) {
//        this.data = data;
//    }
//
    public String getString(String key) {
        return DataUtil.getString(this.get(key), "");
    }

    public int getInt(String key) {
        return DataUtil.getInt(this.get(key), 0);
    }

    public double getDouble(String key) {
        return DataUtil.getDouble(this.get(key), 0.00);
    }
//
//    public Map<String, Object> getData() {
//        return data;
//    }
}
