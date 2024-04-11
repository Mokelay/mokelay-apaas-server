package com.mokelay.core.lego.validator;

import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期类型检查
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("dateTypeCheck")
public class DateTypeCheck implements FieldValidation,ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        String value = inputField.fieldValueToString();
        if (StringUtil.isValid(value)) {
            // 检查是否是yyyy-MM-DD HH:mm:SS  yyyy-MM-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                sdf.parse(value);
            } catch (ParseException e) {
                throw new LegoException("输入参数不是日期类型",ERROR_FIELD_VALIDATE_NOT_DATE_TYPE);
            }
        }
    }
}
