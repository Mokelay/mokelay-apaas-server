package com.mokelay.db.database.sqlserver.testcase;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataList;
import com.mokelay.db.bean.constant.ConT;
import com.mokelay.db.bean.oi.Connector;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;
import com.mokelay.db.util.ConnectorTreeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usagizhang on 17/12/21.
 */
public class ListByConnectorTree {
    public ListByConnectorTree(OIView oiView, RelationalDataManager dataManager) throws DBException {
        DataList dataList = dataManager.list(ConnectorTreeUtil.buildConnectorTree(oiView));
        System.out.println("Data -> " + JSONObject.toJSONString(dataList));
    }


    public ListByConnectorTree(OIView userOIView, OIView accountOIView, RelationalDataManager dataManager) throws DBException {

        Connector connector = new Connector();
        connector.setAlias(userOIView.getOi().getAlias() + "_" + accountOIView.getOi().getAlias());
        connector.setFromFieldName("alias");
        connector.setFromOIAlias(userOIView.getOi().getAlias());
        connector.setToFieldName("userAlias");
        connector.setToOIAlias(accountOIView.getOi().getAlias());

        List<ConnectorTree> connectorTreeList = new ArrayList<ConnectorTree>();
        connectorTreeList.add(ConnectorTreeUtil.buildConnectorTree(accountOIView, connector));
        ConnectorTree queryTree = ConnectorTreeUtil.buildConnectorTree(userOIView, connector, connectorTreeList, ConT.Left);

        DataList dataList = dataManager.list(queryTree);
        System.out.println("Data -> " + JSONObject.toJSONString(dataList));
    }
}
