package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.database.mysql.baseCase.BaseConnectorTreeTest;
import org.junit.Assert;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestCountByConnectorTree extends BaseConnectorTreeTest {

    public TestCountByConnectorTree() throws DBException {

    }

    @Override
    protected void runTest(ConnectorTree connectorTree) throws DBException {
        int count = this.mysqlDataManager.count(queryTree);
        Assert.assertTrue(count > 0);
        this.printJSONObject(count);
    }
}
