package com.greatbee.core.db.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataList;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.core.db.mysql.baseCase.BaseConnectorTreeTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestListByConnectorTree extends BaseConnectorTreeTest implements ExceptionCode {

    public TestListByConnectorTree() throws DBException {

    }

    @Override
    protected void runTest(ConnectorTree connectorTree) throws DBException {
        DataList list = this.mysqlDataManager.list(connectorTree);
        printJSONObject(list);
    }
}
