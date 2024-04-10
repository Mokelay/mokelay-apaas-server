package com.mokelay.db.database.sqlserver.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.RandomGUIDUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.base.bean.constant.DT;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;

import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class CreateData {
    public CreateData(OIView oiView, RelationalDataManager dataManager) throws DBException {
        Field pkField = null;
        List<Field> fields = oiView.getFields();
        for (Field field : fields) {
            if (StringUtil.isValid(field.getDt()) && (field.getDt().equalsIgnoreCase(DT.Date.getType()) || field.getDt().equalsIgnoreCase(DT.Time.getType()))) {
                //插入时间类型的数据
                field.setFieldValue("2001-10-10 20:40:20");
            } else if (StringUtil.isValid(field.getDt()) && (field.getDt().equalsIgnoreCase(DT.INT.getType()) || field.getDt().equalsIgnoreCase(DT.Double.getType()))) {
                //数字类型
                field.setFieldValue("1");
            } else {
                //插入字符串类型
                field.setFieldValue(RandomGUIDUtil.getGUID(RandomGUIDUtil.RANDOM_32));
            }
        }
        String result = dataManager.create(oiView.getOi(), fields);
        System.out.println("Data -> " + result);
    }
}