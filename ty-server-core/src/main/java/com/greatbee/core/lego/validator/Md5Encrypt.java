package com.greatbee.core.lego.validator;

import com.greatbee.base.util.MD5Util;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import org.springframework.stereotype.Component;

/**
 * MD5 加密
 * <p/>
 * Author :CarlChen
 * Date:17/8/4
 */
@Component("md5Encrypt")
public class Md5Encrypt implements FieldValidation, ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        try {
            String value = inputField.getFieldValue().toString();
            inputField.setFieldValue(MD5Util.getMD5(value));
        } catch (Exception e) {
            e.printStackTrace();
            throw new LegoException("MD5加密错误", ERROR_FIELD_VALIDATE_MD5_ERROR);
        }
    }
}
