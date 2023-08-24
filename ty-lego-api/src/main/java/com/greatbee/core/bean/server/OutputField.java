package com.greatbee.core.bean.server;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * Lego output field
 * <p/>
 * Author :CarlChen
 * Date:17/7/8
 */
public class OutputField extends SimpleIND implements IND, AliasBean {
    private Integer apiLegoId;
    private String fieldName;
    private Object fieldValue;

    //必须在整个API中全局唯一
    private String alias;

    //字段类型
    private String oft;

    //是否输入到Http resonse中去
    private boolean response;

    //验证配置，格式为[{handleName:"",params:"aaa,bbb,ccc"}]
    private String handle;

    private String dt;

    private Integer outputFieldDescribeId;

    //输出唯一标示  和  apilego唯一标示
    private String uuid;
    private String apiLegoUuid;

    public Integer getOutputFieldDescribeId() {
        return outputFieldDescribeId;
    }

    public void setOutputFieldDescribeId(Integer outputFieldDescribeId) {
        this.outputFieldDescribeId = outputFieldDescribeId;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Integer getApiLegoId() {
        return apiLegoId;
    }

    public void setApiLegoId(Integer apiLegoId) {
        this.apiLegoId = apiLegoId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getOft() {
        return oft;
    }

    public void setOft(String oft) {
        this.oft = oft;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public OutputField clone() {
        return (OutputField) super.clone();
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApiLegoUuid() {
        return apiLegoUuid;
    }

    public void setApiLegoUuid(String apiLegoUuid) {
        this.apiLegoUuid = apiLegoUuid;
    }
}
