package com.mokelay.core.lego.validator;

import com.mokelay.base.util.MD5Util;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
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
