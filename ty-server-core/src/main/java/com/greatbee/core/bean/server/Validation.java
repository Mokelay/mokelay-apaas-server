package com.greatbee.core.bean.server;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 数据验证Validation
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
public class Validation extends SimpleIND implements IND, AliasBean {
    private String alias;

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
