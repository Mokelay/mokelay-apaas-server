package com.greatbee.core.lego;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.util.SpringContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 输出
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
public class Output implements IO {
    private List<OutputField> outputFields;
    private HttpServletResponse response;
    private HttpServletRequest request;

    public List<OutputField> getOutputFields() {
        return outputFields;
    }

    public Output(HttpServletRequest request, HttpServletResponse response, List<OutputField> outputFields) {
        this.request = request;
        this.response = response;
        this.outputFields = outputFields;
    }

    /**
     * 设置Outfield Value
     *
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public boolean setOutputValue(String fieldName, Object fieldValue) {
        boolean setOk = false;
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField outputField : outputFields) {
                if (outputField.getFieldName().equalsIgnoreCase(fieldName)) {
                    outputField.setFieldValue(fieldValue);
                }
            }
        }
        return setOk;
    }

    /**
     * 验证请求是否有效
     */
    public void handle() throws LegoException {
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField outputField : outputFields) {
                String _val = outputField.getHandle();
                if (StringUtil.isValid(_val)) {
                    JSONArray vals = (JSONArray) JSONArray.parse(_val);
                    int size = vals.size();
                    for (int i = 0; i < size; i++) {
                        String handleName = ((JSONObject) vals.get(i)).getString("handleName");
                        String[] params = null;
                        String _params = ((JSONObject) vals.get(i)).getString("params");
                        if (StringUtil.isValid(_params)) {
                            params = _params.split(",");
                        }
                        FieldHandle fieldHandle = (FieldHandle) SpringContextUtil.getBean(handleName);
                        fieldHandle.handle(this, outputField, params);
                    }
                }
            }
        }
    }

    /**
     * 获取Response List
     *
     * @return
     */
    public List<OutputField> getResponseList() {
        List<OutputField> responseFields = new ArrayList<OutputField>();
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField outputField : outputFields) {
                if (outputField.isResponse()) {
                    responseFields.add(outputField);
                }
            }
        }
        return responseFields;
    }

    /**
     * 根据OFT获取输入字段列表
     *
     * @param oft
     * @return
     */
    public List<OutputField> getOutputField(IOFT oft) {
        List<OutputField> fields = new ArrayList<OutputField>();
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField field : outputFields) {
                if (oft.getType().equalsIgnoreCase(field.getOft())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
