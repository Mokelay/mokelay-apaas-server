package com.mokelay.db.database.sqlserver.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.constant.CT;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;

import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class DeleteByCondition {
    public DeleteByCondition(OIView oiView, RelationalDataManager dataManager) throws DBException {
        Field pkField = null;
        List<Field> fields = oiView.getFields();
        for (Field field : fields) {
            if (field.isPk()) {
                pkField = field;
                break;
            }
        }

        Condition deleteCondition = new Condition();
        deleteCondition.setConditionFieldName("alias");
        deleteCondition.setConditionFieldValue("abc4");
        deleteCondition.setCt(CT.EQ.getName());
        dataManager.delete(oiView.getOi(), deleteCondition);
    }
}
