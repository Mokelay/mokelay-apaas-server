package com.mokelay.db.database.sqlserver.testcase;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;

import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class ReadByPK {
    public ReadByPK(OIView oiView, RelationalDataManager dataManager) throws DBException {
        Field pkField = null;
        List<Field> fields = oiView.getFields();
        for (Field field : fields) {
            if (field.isPk()) {
                pkField = field;
                break;
            }
        }
        pkField.setFieldValue("1");//设置主键值
        Data data = dataManager.read(oiView.getOi(), fields, pkField);
        System.out.println("Data -> " + JSONObject.toJSONString(data));
    }
}
