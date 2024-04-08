package com.greatbee.db.database.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.bean.view.Condition;
import com.greatbee.db.database.mysql.baseCase.BaseConditionTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestDeleteByCondition extends BaseConditionTest {
    public TestDeleteByCondition() throws DBException {
    }

    @Override
    protected void runTest(Condition queryCondition) throws DBException {
        this.mysqlDataManager.delete(mainView.getOi(), queryCondition);
    }
}
