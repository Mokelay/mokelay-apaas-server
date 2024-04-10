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
 * 数据结构校验 不在指定范围值内报错
 * <p/>
 * Author :xiaobc
 * Date:17/7/31
 */
@Component("dataNotInCheck")
public class DataNotInCheck implements FieldValidation,ExceptionCode {

    //多个数据中间用-- 来区
    private  static final String PREX_FLAG="--";

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean openException = false;
        String value = inputField.fieldValueToString();
        if (StringUtil.isValid(value) && params!=null && params.length>0) {
            String[] rangeData = params[0].split(PREX_FLAG);
            openException = true;
            for(int i=0;i<rangeData.length;i++){
                if(value.equals(rangeData[i])){
                    openException = false;
                    break;
                }
            }
        } else if (StringUtil.isInvalid(value)) {
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
                throw new LegoException(inputField.getName() + "不再范围内", ERROR_FIELD_VALIDATE_VALUE_INVALID);
            }
        }
    }
}
