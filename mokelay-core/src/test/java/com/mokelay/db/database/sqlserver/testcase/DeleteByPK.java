package com.mokelay.db.database.sqlserver.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;

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
