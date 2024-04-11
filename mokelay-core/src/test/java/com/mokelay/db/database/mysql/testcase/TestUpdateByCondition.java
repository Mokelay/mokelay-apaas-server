package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.constant.DT;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.database.mysql.baseCase.BaseConditionTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestUpdateByCondition extends BaseConditionTest {
    public TestUpdateByCondition() throws DBException {
    }

    @Override
    protected void runTest(Condition queryCondition) throws DBException {
        List<Field> updateField = new ArrayList<>();
        updateField.add(this.initField(mainView.getOi(),"title","title", DT.String,100));
        this.mysqlDataManager.update(mainView.getOi(), updateField, queryCondition);
    }
}
