package com.mokelay.db.database.mysql.baseCase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.constant.ConT;
import com.mokelay.db.bean.oi.Connector;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.database.mysql.MysqlRelationalDataManagerTest;
import com.mokelay.db.util.ConnectorTreeUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usagizhang on 18/3/14.
 */
public abstract class BaseConnectorTreeTest extends MysqlRelationalDataManagerTest implements ExceptionCode {


    protected ConnectorTree queryTree;

    public BaseConnectorTreeTest() throws DBException {
        this.setUp();
        this.settingOIView();
        this.initConnectorTree();
    }

    @Test
    public void startTest() throws DBException {
        this.runTest(queryTree);
    }

    protected abstract void runTest(ConnectorTree connectorTree) throws DBException;

    public void initConnectorTree() {
        Connector connector = new Connector();
        connector.setAlias("test_connector");
        connector.setName("test_connector");
        connector.setDescription("test_connector");
        connector.setFromFieldName("categoryId");
        connector.setFromOIAlias(mainView.getOi().getAlias());
        connector.setToFieldName("categoryId");
        connector.setToOIAlias(subView.getOi().getAlias());
        List<ConnectorTree> connectorTreeList = new ArrayList<ConnectorTree>();
        connectorTreeList.add(ConnectorTreeUtil.buildConnectorTree(subView, connector));
        queryTree = ConnectorTreeUtil.buildConnectorTree(mainView, connector, connectorTreeList, ConT.Left);

    }


}
