package com.greatbee.db.database.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.bean.constant.DT;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.database.mysql.baseCase.BasePKFieldTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestUpdateByPK extends BasePKFieldTest {
    public TestUpdateByPK() throws DBException {
    }

    @Override
    protected void runTest(Field pkField) throws DBException {
        List<Field> updateField = new ArrayList<>();
        updateField.add(this.initField(mainView.getOi(), "title", "title", DT.String, 256));
        this.mysqlDataManager.update(mainView.getOi(), updateField, pkField);
    }
}
