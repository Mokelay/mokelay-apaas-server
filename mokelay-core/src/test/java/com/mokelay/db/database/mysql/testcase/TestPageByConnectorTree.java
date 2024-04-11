package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataPage;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.database.mysql.baseCase.BaseConnectorTreeTest;

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
