package com.greatbee.core.bean.client;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

import java.util.Date;

/**
 * 模块
 * <p/>
 * 1. 模块是由多个积木构建
 * 2. 模块支持横排竖排两种排版(1.0版本)
 * 3. 模块的属性来自积木的属性
 * 4. 模块的方法来自积木的方法
 * 5. 模块的时间来自积木的事件
 * 6. 偏应用类设计
 * <p/>
 * Author: CarlChen
 * Date: 2018/1/25
 */
public class Module extends SimpleIND implements IND, AliasBean, CreateDateBean {
    private Date createDate;
    private String alias;

    //数据源
    private String ds;
    //模块内容
    private String content;

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public String getDs() {
        return ds;
    }

    public String getContent() {
        return content;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
