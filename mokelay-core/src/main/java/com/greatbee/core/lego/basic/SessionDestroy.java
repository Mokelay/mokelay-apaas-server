package com.greatbee.core.lego.basic;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Session销毁
 *
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("sessionDestroy")
public class SessionDestroy implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        HttpSession session = input.getRequest().getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
