package com.mokelay.core.lego.validator;

import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验 匹配上报错
 * <p/>
 * Author :xiaobc
 * Date:17/7/31
 */
@Component("regularMatchCheck")
public class RegularMatchCheck implements FieldValidation,ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean openException = false;
        String value = inputField.fieldValueToString();
        if(params!=null&&params.length>0) {
            Pattern pattern = Pattern.compile(params[0]);
            Matcher matcher = pattern.matcher(value);
            openException = matcher.matches();
        }

        if(openException){
            if(params!=null&&params.length>1){
                if(LegoUtil.isNumeric(params[1])){
                    throw new LegoException(params[1], DataUtil.getLong(params[1],ERROR_FIELD_VALIDATE_VALUE_INVALID));
                }else {
                    throw new LegoException(params[1], ERROR_FIELD_VALIDATE_VALUE_INVALID);
                }
            }else{
                throw new LegoException(inputField.getName() + "值无效", ERROR_FIELD_VALIDATE_VALUE_INVALID);
            }
        }
    }
}
