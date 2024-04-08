package com.greatbee.procut;

import com.greatbee.db.ExceptionCode;
import com.greatbee.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TY Controller
 * <p>
 * Author: CarlChen
 * Date: 2017/9/15
 */
@Controller
public class TYController implements ExceptionCode {

    private static final Logger logger = Logger.getLogger(TYController.class);

    @Autowired
    private TYDriver tyDriver;

    /**
     * 执行TY的业务逻辑
     *
     * @param request
     * @param response
     * @param apiAlias
     * @return
     */
    protected Response execute(HttpServletRequest request, HttpServletResponse response, String apiAlias) {
        return TYUtil.executeAPIAlias(apiAlias,tyDriver,request,response,false);
    }


}
