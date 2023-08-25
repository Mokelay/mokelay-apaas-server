package com.greatbee.core.lego.validator;

import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 数据结构校验 不等于指定值报错
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
@Component("dataNotEqCheck")
public class DataNotEqCheck implements FieldValidation,ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean openException = false;
        String value = inputField.fieldValueToString();
        if ((value==null&& !"null".equalsIgnoreCase(params[0].toLowerCase()))||(StringUtil.isValid(value) && !value.equals(params[0]))) {
            openException = true;
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
