package com.greatbee.core.lego.basic;

import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 根据输入的Condition，需要判断是否唯一，即取的data为空，如果不为空，则抛出异常
 * <p/>
 * 输入：
 * 1. common 请求参数
 * 2. tpl  Input_Key_Tpl
 * 输出：
 * 1. 模板转换后的String  Output_Key_Tpl_Data
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("tpl")
public class Tpl extends BaseRead implements Lego {

    private static final String Input_Key_Tpl = "tpl";

    private static final String Output_Key_Tpl_Data = "tplData";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String tpl = input.getInputValue(Input_Key_Tpl);
        Map<String, Object> params = LegoUtil.buildTPLParams(input.getRequest(), input.getInputField(IOFT.Common), null,input);
        String result = LegoUtil.transferInputValue(tpl, params);
        output.setOutputValue(Output_Key_Tpl_Data, result);
    }
}
