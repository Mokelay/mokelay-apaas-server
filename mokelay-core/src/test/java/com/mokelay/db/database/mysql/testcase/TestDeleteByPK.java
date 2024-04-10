package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.database.mysql.baseCase.BasePKFieldTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestDeleteByPK extends BasePKFieldTest {

    public TestDeleteByPK() throws DBException {
    }

    @Override
    protected void runTest(Field pkField) throws DBException {
        this.mysqlDataManager.delete(mainView.getOi(), pkField);
    }
}
