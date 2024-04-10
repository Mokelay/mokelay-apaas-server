package com.mokelay.core.lego.system;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * Properties 读取
 *
 * @author CarlChen
 * @date 2018-07-28
 */
@Component("propertiesRead")
public class PropertiesRead implements Lego {
    private static final String Input_Key_Properties_Name = "properties_name";

    private static final String Output_Key_Properties_Value = "properties_value";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String name = input.getInputValue(Input_Key_Properties_Name);
        output.setOutputValue(Output_Key_Properties_Value, TYPPC.getTYProp(name));
    }
}