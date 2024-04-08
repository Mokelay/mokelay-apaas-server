package com.greatbee.db.database.sqlserver.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.view.OIView;
import com.greatbee.db.database.RelationalDataManager;

import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class DeleteByPK {
    public DeleteByPK(OIView oiView, RelationalDataManager dataManager) throws DBException {
        Field pkField = null;
        List<Field> fields = oiView.getFields();
        for (Field field : fields) {
            if (field.isPk()) {
                pkField = field;
                break;
            }
        }
        pkField.setFieldValue("5");//设置主键值
        dataManager.delete(oiView.getOi(), pkField);
    }
}
