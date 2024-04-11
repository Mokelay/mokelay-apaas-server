package com.mokelay.core.lego.validator;

import com.mokelay.base.util.ArrayUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * 数字调整的校验：参数传  +1 或者  -1 （表示在请求数值上加减多少）
 * <p>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("numberTransfer")
public class NumberTransfer implements FieldValidation, ExceptionCode {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        if (ArrayUtil.isValid(params)) {
            long num = DataUtil.getLong(params[0], 0);
            long srcNum = DataUtil.getLong(inputField.fieldValueToString(), 0);
            long newNum = srcNum + num;
            inputField.setFieldValue(newNum);
        }
    }
}
