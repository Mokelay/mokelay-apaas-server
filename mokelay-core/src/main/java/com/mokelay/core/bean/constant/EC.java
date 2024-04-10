package com.mokelay.core.bean.constant;

/**
 * Event Category
 * <p/>
 * Created by CarlChen on 16/10/13.
 */
public enum EC {
    ;
    private TT tt;
    private String category;

    EC(TT tt, String category) {
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
