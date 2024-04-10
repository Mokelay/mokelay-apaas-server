package com.mokelay.core.lego.basic;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.base.util.DataUtil;
import com.mokelay.core.lego.*;
import com.mokelay.api.util.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 乐高制作
 * <p/>
 * 输入：
 * 1. APILego ID
 * 2. OI Alias
 * 3. legoGenerator(目前LegoGenerate实现都是在Lego方法中，所以等同LegoAlias)
 * 输出：
 * <p/>
 * 暂无
 * Author: CarlChen
 * Date: 2017/11/27
 */
@Component("legoBuilder")
public class LegoBuilder implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        int apiLegoId = DataUtil.getInt(input.getInputValue("apiLegoId"), 0);
        String oiAlias = input.getInputValue("oiAlias");
        String legoGenerator = input.getInputValue("legoGenerator");

        ApplicationContext wac = SpringContextUtil.getApplicationContext();
        LegoGenerator _legoGenerator = (LegoGenerator) wac.getBean(legoGenerator);

        if (_legoGenerator != null) {
            _legoGenerator.generate(apiLegoId, oiAlias);
        }
    }
}
