package com.greatbee.db.database.sqlserver.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.constant.CT;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.base.bean.view.Condition;
import com.greatbee.db.bean.view.OIView;
import com.greatbee.db.database.RelationalDataManager;

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
