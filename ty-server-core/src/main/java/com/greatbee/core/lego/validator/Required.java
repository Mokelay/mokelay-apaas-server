package com.greatbee.core.lego.validator;

import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 必填验证器
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("required")
public class Required implements FieldValidation, ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        String fieldValue = inputField.fieldValueToString();
        String errorMsg = "";
        if(params!=null&&params.length>0){
            errorMsg = params[0];
        }
        if (StringUtil.isInvalid(fieldValue)) {
            if(StringUtil.isValid(errorMsg)){
                if(LegoUtil.isNumeric(errorMsg)){
                    throw new LegoException(errorMsg, DataUtil.getLong(errorMsg, ERROR_FIELD_VALIDATE_VALUE_INVALID));
                }else {
                    throw new LegoException(errorMsg, ERROR_FIELD_VALIDATE_REQUIRED);
                }
            }else {
                throw new LegoException(inputField.getName() + "为必填字段", ERROR_FIELD_VALIDATE_REQUIRED);
            }
        }
    }
}
