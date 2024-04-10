package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.database.mysql.baseCase.BaseConditionTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestListByCondition extends BaseConditionTest {
    public TestListByCondition() throws DBException {
    }

    @Override
    protected void runTest(Condition queryCondition) throws DBException {
        DataList dataList = this.mysqlDataManager.list(mainView.getOi(), mainView.getFields(), queryCondition);
        printJSONObject(dataList);
    }
}
