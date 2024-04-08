package com.greatbee.core.lego.validator;

import com.greatbee.base.util.DataUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验 没有匹配上报错
 * <p/>
 * Author :xiaobc
 * Date:17/7/31
 */
@Component("regularNotMatchCheck")
public class RegularNotMatchCheck implements FieldValidation,ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean openException = false;
        String value = inputField.fieldValueToString();
        if(params!=null&&params.length>0) {
            Pattern pattern = Pattern.compile(params[0]);
            Matcher matcher = pattern.matcher(value);
            openException = !matcher.matches();//没有匹配上 报错
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
