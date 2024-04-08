package com.greatbee.core.lego.compute;

import com.greatbee.db.ExceptionCode;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 数组合并
 * <p/>
 * 输入：
 * 1.
 * 输出：
 * 1.
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("arrayMerge")
public class ArrayMerge implements Lego, ExceptionCode {
    @Override
    public void execute(Input input, Output output) throws LegoException {
    }
}
