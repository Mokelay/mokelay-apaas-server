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
 * 整数类型检查
 *
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("integerTypeCheck")
public class IntegerTypeCheck implements FieldValidation,ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        // 检查是否是整数，例如 4 5，-1，但是带小数，字符串等就抛异常
        String value = inputField.fieldValueToString();

        Pattern pattern = Pattern.compile("[-+]?[0-9]*");
        Matcher isNum = pattern.matcher(value);
        if( !isNum.matches() ){
            throw new LegoException("参数不是整数",ERROR_FIELD_VALIDATE_NOT_INTEGER);
        }
    }
}