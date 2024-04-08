package com.greatbee.core.lego;

import com.greatbee.api.lego.LegoException;

/**
 * 乐高自动生成器
 * <p/>
 * Author: CarlChen
 * Date: 2017/11/27
 */
public interface LegoGenerator {


    /**
     * 生成
     * 用于自动生成对应的InputField和OutputField
     */
    public void generate(int apiLegoId, String oiAlias) throws LegoException;
}
