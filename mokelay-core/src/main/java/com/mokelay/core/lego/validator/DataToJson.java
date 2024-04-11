package com.mokelay.core.lego.validator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
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
