package com.greatbee.core.lego.validator;

import com.greatbee.core.lego.FieldValidation;
import com.greatbee.core.lego.Input;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.LegoException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 给输入字段赋予系统默认时间
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("systemTimeAssign")
public class SystemTimeAssign implements FieldValidation {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        // 把系统时间格式化为yyyy-MM-dd HH:mm:SS 设置到fieldValue
        SimpleDateFormat sdf = new SimpleDateFormat((params!=null&&params.length>0)?params[0]:"yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(new Date(System.currentTimeMillis()));
        inputField.setFieldValue(nowStr);
    }
}
