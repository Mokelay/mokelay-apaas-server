package com.greatbee.api.lego;

import com.greatbee.api.bean.server.InputField;

/**
 * 字段验证接口
 * <p/>
 * Author :CarlChen
 * Date:17/7/12
 */
public interface FieldValidation {
    /**
     * 验证字段
     *
     * @param input      Input对象
     * @param inputField 输入字段
     * @param params     参数
     * @throws LegoException
     */
    public void validate(Input input, InputField inputField, String[] params) throws LegoException;
}
