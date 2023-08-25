package com.greatbee.core.lego;

/**
 * 乐高积木
 * <p/>
 * Created by CarlChen on 2017/5/31.
 */
public interface Lego {
    /**
     * 执行
     */
    public void execute(Input input,Output output) throws LegoException;
}