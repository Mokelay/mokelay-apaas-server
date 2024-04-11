package com.mokelay.core.lego.validator;

import com.mokelay.base.util.ArrayUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.util.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 自定义校验  参数是自定义beanID
 * <p>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("customCheck")
public class CustomCheck implements FieldValidation, ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        if (ArrayUtil.isValid(params)) {
            //暂时去掉多个自定义validate校验，后面想想参数怎么传
            String beanId = DataUtil.getString(params[0], null);
            if (StringUtil.isValid(beanId)) {
                ApplicationContext ac = SpringContextUtil.getApplicationContext();
                FieldValidation fieldValidation = (FieldValidation) ac.getBean(beanId);
                fieldValidation.validate(input, inputField, params);
            }
        }
    }
}
