package com.mokelay.db.bean.view;

import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;

import java.util.List;

/**
 * OI View
 * <p/>
 * Created by CarlChen on 2017/5/25.
 */
public class OIView {
    private OI oi;
    private List<Field> fields;

    public OI getOi() {
        return oi;
    }

    public void setOi(OI oi) {
        this.oi = oi;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
