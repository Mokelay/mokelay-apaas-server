package com.greatbee.core.lego.controller;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.constant.ExecuteStatus;
import com.greatbee.core.lego.*;
import org.springframework.stereotype.Component;

/**
 * 常用流程控制器
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/6
 */
@Component("commonProcessController")
public class CommonProcessController implements ProcessController, ExceptionCode {
    @Override
    public ExecuteStatus controller(Input input, Output output, Context context, LegoException e) {
        if (e.getCode() == CODE_Process_Interrupt) {
            return ExecuteStatus.Interrupt;
        } else if (e.getCode() == CODE_Process_Continue) {
            return ExecuteStatus.Continue;
        } else {
            return ExecuteStatus.Error;
        }
    }
}
