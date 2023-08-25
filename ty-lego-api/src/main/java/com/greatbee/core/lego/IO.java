package com.greatbee.core.lego;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Input & Output
 * <p/>
 * Author :CarlChen
 * Date:17/7/23
 */
public interface IO {
    /**
     * Response
     *
     * @return
     */
    public HttpServletResponse getResponse();

    /**
     * Request
     *
     * @return
     */
    public HttpServletRequest getRequest();
}
