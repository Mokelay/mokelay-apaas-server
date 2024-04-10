package com.mokelay.core.lego.controller;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.db.ExceptionCode;
import com.mokelay.core.bean.constant.ExecuteStatus;
import com.mokelay.core.lego.*;
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
