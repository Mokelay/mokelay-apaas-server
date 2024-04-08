package com.greatbee.core.bean.constant;

/**
 * Attribute Category
 * <p/>
 * Created by CarlChen on 16/10/13.
 */
public enum AC {
    ;
    private TT tt;
    private String category;

    AC(TT tt, String category) {
        this.tt = tt;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public TT getTt() {
        return tt;
    }
}
