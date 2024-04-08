package com.greatbee.core.lego.wx;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * wxPayNotifyEnd
 *
 * 微信支付 回调 停止微信轮训通知后台 lego
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("wxPayNotifyEnd")
public class WxPayNotifyEnd extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxPayNotifyEnd.class);

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        HttpServletResponse res = input.getResponse();
        try {
            res.getWriter().println("SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
