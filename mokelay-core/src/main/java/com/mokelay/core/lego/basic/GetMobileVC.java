package com.mokelay.core.lego.basic;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;

/**
 * 获取手机验证码
 * <p/>
 * 输入：
 * 1. 手机号 Input_Key_Mobile
 * 输出：
 * 2. 是否发送成功
 * Author :CarlChen
 * Date:17/7/20
 */
public class GetMobileVC implements Lego {
    private static final String Input_Key_Mobile = "mobile";
    private static final String Output_Key_SEND_OK = "send_ok";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //TODO
    }
}
