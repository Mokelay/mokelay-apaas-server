package com.greatbee.base.bean;

import java.io.Serializable;

/**
 * 被标识的对象
 *
 * Created by CarlChen on 16/10/11.
 */
public interface Identified extends Serializable {
    public static final String BEAN_NAME = "id";

    /**
     * 返回实体唯一标识
     *
     * @return 标识
     */
    public Integer getId();
}
