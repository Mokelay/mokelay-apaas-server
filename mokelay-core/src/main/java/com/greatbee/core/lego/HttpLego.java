package com.greatbee.core.lego;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Http Lego
 * <p/>
 * Author: CarlChen
 * Date: 2018/1/30
 */
public abstract class HttpLego implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        try {
            this.service(input.getRequest(), input.getResponse());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
