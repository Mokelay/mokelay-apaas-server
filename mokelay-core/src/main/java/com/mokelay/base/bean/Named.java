package com.mokelay.base.bean;

/**
 * 有名字的对象
 *
 * Created by CarlChen on 16/10/11.
 */
public interface Named {
    public static final String BEAN_NAME = "name";

    /**
     * 返回对象名称
     *
     * @return 对象名称
     */
    public String getName();
}
