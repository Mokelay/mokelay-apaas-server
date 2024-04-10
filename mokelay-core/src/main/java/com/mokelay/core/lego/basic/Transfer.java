package com.mokelay.core.lego.basic;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 数据迁移
 * TODO 定义输入，输出
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
@Component("transfer")
public class Transfer implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {

    }
}
