package com.mokelay.core.bean.buzz;

import com.mokelay.base.bean.IND;
import com.mokelay.base.bean.ext.SimpleIND;

/**
 * 代码模板
 * <p/>
 * Author: CarlChen
 * Date: 2017/9/4
 */
public class CodeTemplate extends SimpleIND implements IND {
    private String type;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
