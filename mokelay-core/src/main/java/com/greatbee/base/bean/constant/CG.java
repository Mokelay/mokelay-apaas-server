package com.greatbee.base.bean.constant;

/**
 * Condition Group
 * Author :CarlChen
 * Date:17/7/12
 */
public enum CG {
    AND, OR;

    public static CG getCGByName(String name) {
        CG target = null;
        CG[] l = CG.values();
        for (CG cg : l) {
            if (cg.name().equalsIgnoreCase(name)) {
                target = cg;
            }
        }

        return target;
    }
}
