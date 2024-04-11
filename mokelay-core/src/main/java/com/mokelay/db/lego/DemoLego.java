package com.mokelay.db.lego;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.bean.server.InputField;
import org.springframework.stereotype.Component;

/**
 * Created by usagizhang on 18/4/3.
 */
@Component("demoLego")
public class DemoLego implements Lego {



    @Override
    public void execute(Input input, Output output) throws LegoException {
        InputField usernameField = input.getInputField("username");
        InputField passwordField = input.getInputField("password");

        usernameField.setFieldValue(StringUtil.getString(usernameField.getFieldValue()).toUpperCase());
        passwordField.setFieldValue(StringUtil.getString(passwordField.getFieldValue()).toUpperCase());

        output.setOutputValue("usernameField", usernameField.getFieldValue());
        output.setOutputValue("passwordField", passwordField.getFieldValue());
    }
}
