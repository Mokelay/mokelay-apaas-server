package com.greatbee.core.lego.validator;

import com.greatbee.base.util.ArrayUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 数值范围检查
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("numRangeCheck")
public class NumRangeCheck implements FieldValidation,ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        // 判断数字大小范围，不在范围内，则抛异常
        String value = inputField.fieldValueToString();
        double dValue = DataUtil.getDouble(value,-1);
        if(dValue==-1){
            throw new LegoException("请求参数不是数值类型",ERROR_FIELD_VALIDATE_NOT_NUMBER);
        }
        if (ArrayUtil.isValid(params)) {
            double minNum = DataUtil.getDouble(params[0], -1);
            double maxNum = DataUtil.getDouble(params[1], -1);
            if(minNum>-1&& dValue<minNum){
                throw new LegoException("请求参数值不在校验长度范围内", ERROR_FIELD_VALIDATE_OVER_VALUE);
            }
            if(maxNum>-1 && dValue>maxNum){
                throw new LegoException("请求参数值不在校验长度范围内",ERROR_FIELD_VALIDATE_OVER_VALUE);
            }
        } else {
            throw new LegoException("字段参数错误", ERROR_FIELD_VALIDATE_PARAMS_INVALID);
        }
    }
}
