package com.greatbee.core.bean.server;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 数据处理Handler
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
public class Handle extends SimpleIND implements IND, AliasBean {
    private String alias;

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
