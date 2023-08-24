package com.greatbee.core.bean.server;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * OutputField Describe
 * TODO
 * <p/>
 * Author :CarlChen
 * Date:17/7/12
 */
public class OutputFieldDescribe extends SimpleIND implements IND {
    private String legoAlias;
    private String fieldName;

    private String oft;
    //是否是多个，如果是单个，自动导入FieldName进InputField
    private Boolean multiple;

    public String getLegoAlias() {
        return legoAlias;
    }

    public void setLegoAlias(String legoAlias) {
        this.legoAlias = legoAlias;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOft() {
        return oft;
    }

    public void setOft(String oft) {
        this.oft = oft;
    }

    public Boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }
}
