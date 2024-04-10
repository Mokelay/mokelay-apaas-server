package com.mokelay.core.lego.handle;

import com.mokelay.base.util.ArrayUtil;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 日期格式化
 * <p/>
 * Author :CarlChen
 * Date:17/7/25
 */
@Component("dateFormat")
public class DateFormat implements FieldHandle {

    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();

        String format = "yyyy-MM-dd HH:mm:ss";
        if (ArrayUtil.isValid(params)) {
            format = params[0];
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newVal = sdf.format(value);
        outputField.setFieldValue(newVal);
    }
}
