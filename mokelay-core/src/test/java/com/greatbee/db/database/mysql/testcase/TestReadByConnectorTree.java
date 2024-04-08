package com.greatbee.db.database.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.database.mysql.baseCase.BaseConnectorTreeTest;


/**
 * Created by usagizhang on 18/3/14.
 */
public class TestReadByConnectorTree extends BaseConnectorTreeTest {

    public TestReadByConnectorTree() throws DBException {

    }

    @Override
    protected void runTest(ConnectorTree connectorTree) throws DBException {
        Data data = this.mysqlDataManager.read(queryTree);
        printJSONObject(data);
    }
}
