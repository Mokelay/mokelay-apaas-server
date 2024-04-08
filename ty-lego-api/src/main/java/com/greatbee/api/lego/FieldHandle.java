package com.greatbee.api.lego;

import com.greatbee.api.bean.server.OutputField;

/**
 * 字段处理
 * <p/>
 * Author :CarlChen
 * Date:17/7/23
 */
public interface FieldHandle {

    /**
     * 字段处理
     *
     * @param output
     * @param outputField
     * @param params
     * @throws LegoException
     */
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException;
}
