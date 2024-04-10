package com.mokelay.core.lego.validator;

import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.core.lego.util.LegoUtil;
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
