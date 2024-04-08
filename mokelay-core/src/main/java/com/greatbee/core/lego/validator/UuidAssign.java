package com.greatbee.core.lego.validator;

import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
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
