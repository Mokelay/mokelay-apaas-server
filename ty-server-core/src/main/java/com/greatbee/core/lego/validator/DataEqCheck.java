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
 * 数据结构校验 等于指定值报错
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
@Component("dataEqCheck")
public class DataEqCheck implements FieldValidation,ExceptionCode {

    /**
     * params[0] 为比较对象字符串
     * params[1] 为比较出错后的异常信息
     *
     * @param input      Input对象
     * @param inputField 输入字段
     * @param params     参数
     * @throws LegoException
     */
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean openException = false;
        String value = inputField.fieldValueToString();
        if ((StringUtil.isValid(value) && value.equals(params[0]))||(value==null&&"null".equalsIgnoreCase(params[0].toLowerCase()))) {
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
