package com.greatbee.core.bean.buzz;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * BUZZ 代码
 * <p/>
 * Author: CarlChen
 * Date: 2017/9/4
 */
public class Code extends SimpleIND implements IND, CreateDateBean {
    private String alias;
    private Date createDate;
    private String code;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCode() {
        return code;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
