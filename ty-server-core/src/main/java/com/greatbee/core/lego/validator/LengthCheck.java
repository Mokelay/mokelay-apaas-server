package com.greatbee.core.lego.validator;

import com.greatbee.base.util.ArrayUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 字段长度判断
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("lengthCheck")
public class LengthCheck implements FieldValidation, ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        String value = inputField.fieldValueToString();
        int len = value.length();
        // 长度检查，如果超出长度，或者小于长度，则抛异常
        if (ArrayUtil.isValid(params)) {
            int minLength = DataUtil.getInt(params[0], -1);
            int maxLength = DataUtil.getInt(params[1], -1);
            if(minLength>-1&& len<minLength){
                throw new LegoException("请求参数长度不在校验长度范围内",ERROR_FIELD_VALIDATE_OVER_LENGTH);
            }
            if(maxLength>-1 && len>maxLength){
                throw new LegoException("请求参数长度不在校验长度范围内",ERROR_FIELD_VALIDATE_OVER_LENGTH);
            }
        } else {
            throw new LegoException("字段参数错误", ERROR_FIELD_VALIDATE_PARAMS_INVALID);
        }
    }
}
