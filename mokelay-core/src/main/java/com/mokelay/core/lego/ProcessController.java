package com.mokelay.core.lego;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.bean.constant.ExecuteStatus;

/**
 * 流程控制器
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/6
 */
public interface ProcessController {
    public static final String Default_Process_Controller = "commonProcessController";

    /**
     * 判断控制器
     */
    public ExecuteStatus controller(Input input, Output output, Context context, LegoException e);
}
