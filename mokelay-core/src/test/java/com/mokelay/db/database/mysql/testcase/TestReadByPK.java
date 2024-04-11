package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.database.mysql.baseCase.BasePKFieldTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestReadByPK extends BasePKFieldTest {
    public TestReadByPK() throws DBException {
    }

    @Override
    protected void runTest(Field pkField) throws DBException {
        Data data = this.mysqlDataManager.read(mainView.getOi(), mainView.getFields(), pkField);
        printJSONObject(data);
    }
}
