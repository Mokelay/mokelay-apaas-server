package com.mokelay.core.lego.validator;

import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字检查
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("digitalCheck")
public class DigitalCheck implements FieldValidation,ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        // 检查是否是数字，例如 4 5，-1，-5.23
        String value = inputField.fieldValueToString();

        Pattern pattern = Pattern.compile("[-+]?[0-9]*[\\.]?[0-9]*");
        Matcher isNum = pattern.matcher(value);
        if( !isNum.matches() ){
            throw new LegoException("参数不是数字",ERROR_FIELD_VALIDATE_NOT_NUMBER);
        }

    }
}
