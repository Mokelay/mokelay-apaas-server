package com.greatbee.core.lego;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.APILego;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.util.SpringContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输入
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
public class Input implements IO {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<InputField> inputFields;
    private APILego apiLego;
    private Map<String,InputField> inputFieldsMap;


    public Input(){}
    public Input(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

//    public static void main(String args[]) {
//        JSONArray vals = (JSONArray) JSONArray.parse("[{validateName:\"ffff\",params:\"aaa,bbb,ccc\"},{validateName:\"ggg\",params:\"111,22,33\"}]");
//
//        System.out.println(vals);
//    }

    /**
     * 验证请求是否有效
     */
    public void validate() throws LegoException {
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                String _val = inputField.getValidate();
                if (StringUtil.isValid(_val)) {
                    JSONArray vals = (JSONArray) JSONArray.parse(_val);
                    int size = vals.size();
                    for (int i = 0; i < size; i++) {
                        String validateName = ((JSONObject) vals.get(i)).getString("validateName");
                        String[] params = null;
                        String _params = ((JSONObject) vals.get(i)).getString("params");
                        if (StringUtil.isValid(_params)) {
                            params = _params.split(",");
                        }
//                        ApplicationContext ac = LegoUtil.getApplicationContext(request);
//                        FieldValidation fieldValidation = (FieldValidation) ac.getBean(validateName);
                        FieldValidation fieldValidation = (FieldValidation) SpringContextUtil.getBean(validateName);
                        fieldValidation.validate(this, inputField, params);
                    }
                }
            }
        }
    }

    /**
     * 获取输入字段列表
     *
     * @return
     */
    public List<InputField> getInputFields() {
        return inputFields;
    }

    /**
     * 获取API Lego对象
     *
     * @return
     */
    public APILego getApiLego() {
        return apiLego;
    }

    public void setApiLego(APILego apiLego) {
        this.apiLego = apiLego;
    }

    public void setInputFields(List<InputField> inputFields) {
        this.inputFields = inputFields;
    }

    /**
     * 获取Lego配置参数
     *
     * @param name
     * @return
     */
    public String getLegoAttribute(String name) {
        String attrStr = apiLego.getAttributes();
        if (StringUtil.isValid(attrStr)) {
            JSONObject o = (JSONObject) JSON.parse(attrStr);
            return o.getString(name);
        }
        return null;
    }

    /**
     * 通过Name获取Input的值
     *
     * @param name
     * @return
     */
    public String getInputValue(String name) {
        InputField target = null;
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                String key = inputField.getAlias();
                if (StringUtil.isInvalid(key)) {
                    key = inputField.getFieldName();
                }
                if (key.equalsIgnoreCase(name)) {
                    target = inputField;
                    break;
                }
            }
        }
        return target != null ? target.fieldValueToString() : null;
    }

    public Object getInputObjectValue(String name) {
        Object inputValue = null;
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                String key = inputField.getAlias();
                if (StringUtil.isInvalid(key)) {
                    key = inputField.getFieldName();
                }
                if (key.equalsIgnoreCase(name)) {
                    inputValue = inputField.getFieldValue();
                    break;
                }
            }
        }
        return inputValue;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 获取InputField
     *
     * @param name
     * @return
     */
    public InputField getInputField(String name) {
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                String key = inputField.getAlias();
                if (StringUtil.isInvalid(key)) {
                    key = inputField.getFieldName();
                }

                if (key.equalsIgnoreCase(name)) {
                    return inputField;
                }
            }
        }
        return null;
    }

    /**
     * 根据IFT获取输入字段列表
     *
     * @param ift
     * @return
     */
    public List<InputField> getInputField(IOFT ift) {
        List<InputField> fields = new ArrayList<InputField>();
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField field : inputFields) {
                if (ift.getType().equalsIgnoreCase(field.getIft())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    /**
     * inputField alias 作为key
     * 获取input 的所有inputField
     * @return
     */
    public Map<String, InputField> getInputFieldsMap() {
        Map<String,InputField> map = new HashMap<>();
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField field : inputFields) {
                map.put(field.getAlias(),field);
            }
        }
        return map;
    }

}
