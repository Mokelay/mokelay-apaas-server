package com.mokelay.db.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.AbstractBasicManager;
import com.mokelay.base.bean.constant.CT;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.bean.view.MultiCondition;
import com.mokelay.db.manager.FieldManager;

import java.util.List;

/**
 * Simple OI Manager
 * <p/>
 * Created by CarlChen on 2017/5/24.
 */
public class SimpleFieldManager extends AbstractBasicManager implements FieldManager {
    public SimpleFieldManager() {
        super(Field.class);
    }


    @Override
    public List<Field> getFields(String oiAlias) throws DBException {
        return (List<Field>) this.list("oiAlias", oiAlias);
    }

    @Override
    public Field getField(String oiAlias, String fieldName) throws DBException {
        MultiCondition con = new MultiCondition();
        Condition con1 = new Condition();
        con1.setConditionFieldName("oiAlias");
        con1.setConditionFieldValue(oiAlias);
        con1.setCt(CT.EQ.getName());
        con.addCondition(con1);
        Condition con2 = new Condition();
        con2.setConditionFieldName("fieldName");
        con2.setConditionFieldValue(fieldName);
        con2.setCt(CT.EQ.getName());
        con.addCondition(con2);
        List<Field> fs = (List<Field>) this.list(con);
        return CollectionUtil.isValid(fs) ? fs.get(0) : null;
    }
}
