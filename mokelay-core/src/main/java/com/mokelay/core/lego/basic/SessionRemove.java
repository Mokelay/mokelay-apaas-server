package com.mokelay.core.lego.basic;

import com.mokelay.base.util.CollectionUtil;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
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
