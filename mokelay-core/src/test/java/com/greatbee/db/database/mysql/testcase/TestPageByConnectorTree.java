package com.greatbee.db.database.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataPage;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.database.mysql.baseCase.BaseConnectorTreeTest;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestPageByConnectorTree extends BaseConnectorTreeTest implements ExceptionCode {


    public TestPageByConnectorTree() throws DBException {
    }

    @Override
    protected void runTest(ConnectorTree connectorTree) throws DBException {
        DataPage page = this.mysqlDataManager.page(1, 5, queryTree);
        this.printJSONObject(page);
    }
}
