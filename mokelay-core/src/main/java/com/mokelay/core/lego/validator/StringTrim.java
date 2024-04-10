package com.mokelay.core.lego.validator;

import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
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
