package com.greatbee.core.lego.basic;

import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Session检查
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("sessionCheck")
public class SessionCheck implements Lego, ExceptionCode {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        java.util.List<InputField> checkFields = input.getInputFields();

        HttpSession session = input.getRequest().getSession(false);
        if (session != null) {
            if (CollectionUtil.isValid(checkFields)) {
                for (InputField field : checkFields) {
                    Object value = session.getAttribute(field.getFieldName());
                    if (value == null) {
                        throw new LegoException("会话超时", ERROR_LEGO_SESSION_TIMEOUT);
                    }
                }
            }
        } else {
            throw new LegoException("会话超时", ERROR_LEGO_SESSION_TIMEOUT);
        }
    }
}
