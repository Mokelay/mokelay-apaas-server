package com.mokelay.core.lego.validator;

import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.LegoException;

/**
 * 手机验证码判断
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
public class MobileVCCheck implements FieldValidation {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        //TODO
    }
}
