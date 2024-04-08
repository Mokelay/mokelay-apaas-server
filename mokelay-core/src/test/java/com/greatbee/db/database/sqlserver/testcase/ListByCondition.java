package com.greatbee.db.database.sqlserver.testcase;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.constant.CT;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.base.bean.view.Condition;
import com.greatbee.db.bean.view.OIView;
import com.greatbee.db.database.RelationalDataManager;

import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class ListByCondition {
    public ListByCondition(OIView oiView, RelationalDataManager dataManager) throws DBException {

        List<Field> fields = oiView.getFields();
        Condition queryCondition = new Condition();
        queryCondition.setConditionFieldName("alias");
        queryCondition.setConditionFieldValue("abc");
        queryCondition.setCt(CT.EQ.getName());

        DataList dataList = dataManager.list(oiView.getOi(), fields, queryCondition);
        System.out.println("Data -> " + JSONObject.toJSONString(dataList));
    }
}