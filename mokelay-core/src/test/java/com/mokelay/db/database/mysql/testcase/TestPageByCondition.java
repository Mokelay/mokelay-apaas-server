package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataPage;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.database.mysql.baseCase.BaseConditionTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestPageByCondition extends BaseConditionTest {
    public TestPageByCondition() throws DBException {

    }

    @Override
    protected void runTest(Condition queryCondition) throws DBException {
        DataPage dataPage = this.mysqlDataManager.page(mainView.getOi(), mainView.getFields(), 1, 5, queryCondition);
        printJSONObject(dataPage);
    }
}
