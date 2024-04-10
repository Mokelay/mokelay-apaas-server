package com.mokelay.api.bean.server;

import com.mokelay.base.bean.AliasBean;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.bean.IND;
import com.mokelay.base.bean.ext.SimpleIND;
import com.mokelay.base.util.CollectionUtil;

import java.util.List;

/**
 * API Lego Input
 * <p/>
 * Author :CarlChen
 * Date:17/7/7
 */
public class InputField extends SimpleIND implements IND, AliasBean, Cloneable {
    private Integer apiLegoId;
    private String fieldName;
    //存储字段的值，属于业务逻辑，不存数据库
    private Object fieldValue;
    //默认是EQ，如果是Condition的InputField，那可以配置CT
    private String ct;

    //字段类型
    private String ift;

    //字段值的类型
    private String fvt;

    //是为了当OI+ConnectorPath导致FieldName重复，则通过Alias来进行唯一性判断,Alias在整个API中保持全局唯一
    private String alias;

    //如果FVT == output，则配置来源的APILegoID，通过当前InputField.fromApiLegoAlias = outputField.alias
    private Integer fromApiLegoId;
    private String fromApiLegoOutputFieldAlias;
    //fromApiLegoId 换成了uuid
    private String fromApiLegoUuid;

    //如果FVT == request，则request name从requestParamName中获取
    private String requestParamName;

    //如果FVT == session， 则从sessionParamName中获取session值
    private String sessionParamName;

    //如果FVT == constant ，则fieldValue = constant
    private String constant;

    //JSON Config Data,{name:value} TODO
    private String attributes;

    //验证配置，格式为[{validateName:"",params:"aaa,bbb,ccc"}]
    private String validate;

    //如果是跨OI的字段，则需要记录好连接路径，格式为[{alias:"conn1"},{alias:"conn2"}]
    private String connectorPath;

    private String dt;

    private Integer inputFieldDescribeId;
    //输入字段模板，支持 ${request.name}   ${session.data.sex}   ${node.data2.id} （来自上面节点）
    private String fieldTpl;

    //输入字段 唯一标示  apilego唯一标识
    private String uuid;
    private String apiLegoUuid;

    public String getSessionParamName() {
        return sessionParamName;
    }

    public void setSessionParamName(String sessionParamName) {
        this.sessionParamName = sessionParamName;
    }

    public String getFromApiLegoOutputFieldAlias() {
        return fromApiLegoOutputFieldAlias;
    }

    public void setFromApiLegoOutputFieldAlias(String fromApiLegoOutputFieldAlias) {
        this.fromApiLegoOutputFieldAlias = fromApiLegoOutputFieldAlias;
    }

    public String getRequestParamName() {
        return requestParamName;
    }

    public void setRequestParamName(String requestParamName) {
        this.requestParamName = requestParamName;
    }

    public Integer getInputFieldDescribeId() {
        return inputFieldDescribeId;
    }

    public void setInputFieldDescribeId(Integer inputFieldDescribeId) {
        this.inputFieldDescribeId = inputFieldDescribeId;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Integer getApiLegoId() {
        return apiLegoId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setApiLegoId(Integer apiLegoId) {
        this.apiLegoId = apiLegoId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    /**
     * 如果fieldValue是List类型，就将请求参数转换成string,号隔开,只适用于单字段的list，(in条件组装)
     *
     * @return
     */
    public String fieldValueToString() {
        boolean isList = false;
        List list = null;
        if (fieldValue != null) {
            if (fieldValue instanceof List) {
                list = (List) fieldValue;
                isList = true;
            } else if (fieldValue instanceof DataList) {
                list = ((DataList) fieldValue).getList();
                isList = true;
            }
        }
        if (isList) {
            StringBuilder sb = new StringBuilder();
            if (CollectionUtil.isValid(list)) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        sb.append(",");
                    }
                    sb.append(list.get(i).toString());
                }
            }
            return sb.toString();
        } else {
            return fieldValue != null ? fieldValue.toString() : null;
        }
    }

    public String getFvt() {
        return fvt;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void setFvt(String fvt) {
        this.fvt = fvt;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getIft() {
        return ift;
    }

    public void setIft(String ift) {
        this.ift = ift;
    }

    public Integer getFromApiLegoId() {
        return fromApiLegoId;
    }

    public void setFromApiLegoId(Integer fromApiLegoId) {
        this.fromApiLegoId = fromApiLegoId;
    }

    public String getConnectorPath() {
        return connectorPath;
    }

    public void setConnectorPath(String connectorPath) {
        this.connectorPath = connectorPath;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public InputField clone() {
        return (InputField) super.clone();
    }

    public String getFieldTpl() {
        return fieldTpl;
    }

    public void setFieldTpl(String fieldTpl) {
        this.fieldTpl = fieldTpl;
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

    public String getFromApiLegoUuid() {
        return fromApiLegoUuid;
    }

    public void setFromApiLegoUuid(String fromApiLegoUuid) {
        this.fromApiLegoUuid = fromApiLegoUuid;
    }
}
