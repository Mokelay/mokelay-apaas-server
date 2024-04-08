package com.greatbee.core.lego;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LegoDemo
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("legoDemo")
public class LegoDemo implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(LegoDemo.class);
    @Autowired
    private TYDriver tyDriver;


    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
    }
}
