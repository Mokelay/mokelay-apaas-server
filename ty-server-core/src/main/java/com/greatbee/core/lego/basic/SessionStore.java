package com.greatbee.core.lego.basic;

import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Session 存储
 * <p/>
 * 输入：所定义的Input
 * <p/>
 * 输出：暂无
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("sessionStore")
public class SessionStore implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        HttpSession session = input.getRequest().getSession(false);
        if(session == null){
            session = input.getRequest().getSession(true);
        }
        java.util.List<InputField> fields = input.getInputFields();
        if (CollectionUtil.isValid(fields)) {
            for (InputField field : fields) {
                session.removeAttribute(field.getFieldName());
                session.setAttribute(field.getFieldName(), field.getFieldValue());
            }
        }
    }
}
