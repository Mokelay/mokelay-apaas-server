package com.mokelay.core.bean.constant;

import com.mokelay.base.util.StringUtil;

/**
 * Field Value Type
 * <p/>
 * {constant,expression,request,calculation}
 * <p/>
 * Created by CarlChen on 2016/11/12.
 */
public enum FVT {
    //常量
    Constant("constant"),
    //请求参数
    Request("request"),
    //Session会话
    Session("session"),
    //来自输出
    Output("output"),
    //来自模板
    Template("template"),

    //计算逻辑
    Calculation("calculation"),
    //表达式
    Expression("expression");
    private String type;

    FVT(String type) {
        this.type = type;
    }

    /**
     * 获取FVT
     *
     * @param type
     * @return
     */
    public static FVT getFVT(String type) {
        if (StringUtil.isValid(type)) {
            FVT[] fvts = FVT.values();
            for (FVT fvt : fvts) {
                if (fvt.getType().equalsIgnoreCase(type)) {
                    return fvt;
                }
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
