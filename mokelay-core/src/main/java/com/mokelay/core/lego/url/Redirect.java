package com.mokelay.core.lego.url;

import com.mokelay.core.bean.view.RedirectConfig;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 转发乐高
 */
@Component("redirect")
public class Redirect implements Lego {
    private static final String Input_Key_Url = "url";

    private static final String Output_Key_Redirect_Config = "redirect_config";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String url = input.getInputValue(Input_Key_Url);

        RedirectConfig rc = new RedirectConfig();
        rc.setUrl(url);

        output.setOutputValue(Output_Key_Redirect_Config, rc);
    }
}