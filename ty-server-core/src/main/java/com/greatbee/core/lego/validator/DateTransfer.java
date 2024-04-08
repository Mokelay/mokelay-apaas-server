package com.greatbee.core.lego.validator;

import com.greatbee.base.util.ArrayUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.FieldValidation;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *  日期调整的校验：参数传  +3600 或者  -3600 （表示在请求时间上加减多长时间）
 *  参数1： 3600 / -3600
 *  参数2: yyyy-MM-dd   请求参数的时间格式   默认yyyy-MM-dd HH:mm:ss
 *  参数3: yyyy-MM-dd  需要转换成的时间格式  默认yyyy-MM-dd HH:mm:ss
 * <p>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("dateTransfer")
public class DateTransfer implements FieldValidation, ExceptionCode {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        if (ArrayUtil.isValid(params)) {
            long time = DataUtil.getLong(params[0], 0);
            String date = inputField.fieldValueToString();
            if(StringUtil.isInvalid(date)){
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(params.length>1?params[1]:DEFAULT_DATE_TIME_FORMAT);//请求参数的格式
            SimpleDateFormat sdf2 = new SimpleDateFormat(params.length > 2?params[2]:DEFAULT_DATE_TIME_FORMAT);//转换成的格式
            try {
                long paramTime = sdf.parse(date).getTime();
                String newDate = sdf2.format(paramTime + time);
                inputField.setFieldValue(newDate);
            } catch (ParseException e) {
                throw new LegoException("日期转换异常",ERROR_FIELD_VALIDATE_DATE_TRANSFER);
            }
        }
    }
}
