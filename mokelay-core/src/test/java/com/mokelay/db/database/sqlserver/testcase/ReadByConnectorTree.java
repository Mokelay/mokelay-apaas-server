package com.mokelay.db.database.sqlserver.testcase;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
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
public class ReadByConnectorTree {
    public ReadByConnectorTree(OIView oiView, RelationalDataManager dataManager) throws DBException {

        Data data = dataManager.read(ConnectorTreeUtil.buildConnectorTree(oiView));
        System.out.println("Data -> " + JSONObject.toJSONString(data));
    }

    public ReadByConnectorTree(OIView mainView,OIView subView,RelationalDataManager dataManager) throws DBException{
        Connector connector = new Connector();
        connector.setAlias(mainView.getOi().getAlias() + "_" + subView.getOi().getAlias());
        connector.setFromFieldName("alias");
        connector.setFromOIAlias(mainView.getOi().getAlias());
        connector.setToFieldName("userAlias");
        connector.setToOIAlias(subView.getOi().getAlias());

        List<ConnectorTree> connectorTreeList = new ArrayList<ConnectorTree>();
        connectorTreeList.add(ConnectorTreeUtil.buildConnectorTree(subView, connector));
        ConnectorTree queryTree = ConnectorTreeUtil.buildConnectorTree(mainView, connector, connectorTreeList, ConT.Left);

        Data data = dataManager.read(queryTree);
        System.out.println("Data -> " + JSONObject.toJSONString(data));
    }
}
