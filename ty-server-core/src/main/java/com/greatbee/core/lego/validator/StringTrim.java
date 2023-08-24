package com.greatbee.core.lego.validator;

import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 去除请求参数两头的空格、换行等
 * <p>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("stringTrim")
public class StringTrim implements FieldValidation, ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        String val = inputField.fieldValueToString();
        if(StringUtil.isValid(val)){
            inputField.setFieldValue(val.trim());
        }
    }
}
