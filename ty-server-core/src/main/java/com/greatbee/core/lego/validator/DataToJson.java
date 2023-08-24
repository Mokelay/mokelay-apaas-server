package com.greatbee.core.lego.validator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将Data对象转换成json
 * <p>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("dataToJson")
public class DataToJson implements FieldValidation, ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        Object obj = inputField.getFieldValue();
        if(obj!=null){
            if(obj instanceof List){
                inputField.setFieldValue(JSONArray.toJSONString(obj));
            }else {
                inputField.setFieldValue(JSONObject.toJSONString(obj));
            }
        }
    }
}
