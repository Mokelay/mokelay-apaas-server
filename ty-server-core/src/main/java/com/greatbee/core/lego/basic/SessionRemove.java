package com.greatbee.core.lego.basic;

import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * 删除Session的Attribute
 * <p/>
 * Author :CarlChen
 * Date:17/8/2
 */
@Component("sessionRemove")
public class SessionRemove implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        java.util.List<InputField> removeFields = input.getInputFields();

        HttpSession session = input.getRequest().getSession(false);
        if (session != null) {
            if (CollectionUtil.isValid(removeFields)) {
                for (InputField field : removeFields) {
                    session.removeAttribute(field.getFieldName());
                }
            }
        }
    }
}
