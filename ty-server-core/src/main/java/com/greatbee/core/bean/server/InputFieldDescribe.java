package com.greatbee.core.bean.server;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * InputField描述体
 * <p/>
 * Author :CarlChen
 * Date:17/7/11
 */
public class InputFieldDescribe extends SimpleIND implements IND {
    private String legoAlias;
    private String fieldName;

    //是否是多个，如果是单个，自动导入FieldName进InputField
    private Boolean multiple;
    //是否来源于OI里的字段
    private Boolean fromOIFields;
    private String ift;

    public String getIft() {
        return ift;
    }

    public void setIft(String ift) {
        this.ift = ift;
    }

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

    public Boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Boolean isFromOIFields() {
        return fromOIFields;
    }

    public void setFromOIFields(Boolean fromOIFields) {
        this.fromOIFields = fromOIFields;
    }
}
