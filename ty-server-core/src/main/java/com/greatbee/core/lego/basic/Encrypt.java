package com.greatbee.core.lego.basic;

import com.greatbee.base.util.MD5Util;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.springframework.stereotype.Component;

/**
 * MD5 加密
 * <p/>
 * Author :CarlChen
 * Date:17/8/4
 */
@Component("encrypt")
public class Encrypt implements Lego,ExceptionCode {
    @Override
    public void execute(Input input, Output output) throws LegoException {

        java.util.List<InputField> ifs = input.getInputFields();
        try {
            for (InputField _if : ifs) {
                String value = _if.getFieldValue().toString();
                _if.setFieldValue(MD5Util.getMD5(value));
                //直接把加密的字段放到output中
                output.setOutputValue(_if.getFieldName(),_if.getFieldValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LegoException("MD5加密错误",ERROR_LEGO_VALUE_MD5_ERROR);
        }

    }
}
