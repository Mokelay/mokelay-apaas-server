package com.greatbee.core.lego.validator;

import com.alibaba.fastjson.JSON;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 将json 转换成 对象或者数组
 * <p>
 * Author :xiaobc
 * Date:17/7/17
 */
@Component("jsonToData")
public class JsonToData implements FieldValidation, ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        Object obj = inputField.getFieldValue();
        if(obj!=null&&obj instanceof String){
            inputField.setFieldValue(JSON.parse((String) obj));
        }
    }
}
