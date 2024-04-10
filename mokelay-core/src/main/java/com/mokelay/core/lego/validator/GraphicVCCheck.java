package com.mokelay.core.lego.validator;

import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * 图形验证码判断
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("graphicVCCheck")
public class GraphicVCCheck implements FieldValidation,ExceptionCode {
    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {

        HttpSession session = input.getRequest().getSession();
        if (session != null) {
            String value = session.getAttribute(inputField.getFieldName()).toString();
            if (StringUtil.isInvalid(value)) {
                throw new LegoException("会话超时", ERROR_LEGO_SESSION_TIMEOUT);
            }
            if(!value.equalsIgnoreCase(inputField.getFieldValue().toString())){
                throw new LegoException("验证码错误",ERROR_LEGO_VALUE_VC_ERROR);
            }
        } else {
            throw new LegoException("会话超时", ERROR_LEGO_SESSION_TIMEOUT);
        }
    }
}
