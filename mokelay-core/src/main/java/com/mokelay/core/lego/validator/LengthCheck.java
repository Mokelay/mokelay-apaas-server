package com.mokelay.core.lego.validator;

import com.mokelay.base.util.ArrayUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.LegoException;
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
