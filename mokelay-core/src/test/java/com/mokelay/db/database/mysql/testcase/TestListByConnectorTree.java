package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataList;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.database.mysql.baseCase.BaseConnectorTreeTest;

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
