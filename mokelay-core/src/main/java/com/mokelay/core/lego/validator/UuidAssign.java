package com.mokelay.core.lego.validator;

import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 给输入字段赋予uuid 随机生成数
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("uuidAssign")
public class UuidAssign implements FieldValidation {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        inputField.setFieldValue(UUID.randomUUID());
    }
}
