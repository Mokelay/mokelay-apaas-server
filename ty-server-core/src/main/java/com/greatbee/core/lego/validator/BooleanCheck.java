package com.greatbee.core.lego.validator;


import com.greatbee.base.util.DataUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 值为True的判断
 * <p/>
 * <p/>
 * params[0] 抛出异常的条件
 * params[1] 抛出异常的code
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("booleanCheck")
public class BooleanCheck implements FieldValidation, ExceptionCode {
    private static final String THROW_EXCEPTION_IF_RESULT_TRUE = "THROW_EXCEPTION_IF_RESULT_TRUE";
    private static final String THROW_EXCEPTION_IF_RESULT_FALSE = "THROW_EXCEPTION_IF_RESULT_FALSE";

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        boolean result = false;
        Object value = inputField.getFieldValue();
        if (value instanceof Boolean) {
            //如果值为Boolean，则直接赋值
            result = (boolean) value;
        } else if (value instanceof java.util.List) {
            //如果是列表，则必须所有值为True，最终结构才能是true
            boolean _r = true;
            for (Object o : (java.util.List) value) {
                if (o instanceof Boolean) {
                    if (!((boolean) o)) {
                        _r = false;
                    }
                } else {
                    _r = false;
                }
            }
            result = _r;
        }

        if (params == null || params.length == 0) {
            params = new String[]{THROW_EXCEPTION_IF_RESULT_FALSE};
        }

        boolean throwException = (THROW_EXCEPTION_IF_RESULT_FALSE.equalsIgnoreCase(params[0])) ? (!result) : (result);

        if (throwException) {
            if (params.length > 1) {
                throw new LegoException(DataUtil.getLong(params[1], ERROR_FIELD_VALIDATE_BOOLEAN_CHECK));
            } else {
                throw new LegoException(ERROR_FIELD_VALIDATE_BOOLEAN_CHECK);
            }
        }
    }
}
