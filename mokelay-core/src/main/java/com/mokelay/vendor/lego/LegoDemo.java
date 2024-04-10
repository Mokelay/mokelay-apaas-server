package com.mokelay.vendor.lego;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.db.ExceptionCode;
import com.mokelay.core.manager.TYDriver;
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
