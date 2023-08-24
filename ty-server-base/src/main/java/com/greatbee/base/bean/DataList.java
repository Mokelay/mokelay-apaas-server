package com.greatbee.base.bean;

import com.greatbee.base.util.CollectionUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 列表数据
 *
 * Created by CarlChen on 2016/11/12.
 */
public class DataList implements Serializable{
    private List list;


    public DataList(){}

    public DataList(List list) {
        this.list = list;
    }

    /**
     * 数据列表
     *
     */
    public List getList() {
        return list;
    }

    /**
     * 得到一共有多少条数据
     * @return 数量
     */
    public int getTotalRecords() {
        return CollectionUtil.isValid(list) ? list.size() : 0;
    }

    public void setList(List list) {
        this.list = list;
    }
}
