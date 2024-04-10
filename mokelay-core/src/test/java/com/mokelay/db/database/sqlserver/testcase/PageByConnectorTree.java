package com.mokelay.db.database.sqlserver.testcase;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataPage;
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
public class PageByConnectorTree {
    public PageByConnectorTree(OIView oiView, RelationalDataManager dataManager) throws DBException {
        DataPage dataPage = dataManager.page(1, 10, ConnectorTreeUtil.buildConnectorTree(oiView));
        System.out.println("Data -> " + JSONObject.toJSONString(dataPage));
    }

    public PageByConnectorTree(OIView mainView,OIView subView,RelationalDataManager dataManager) throws DBException {

        Connector connector = new Connector();
        connector.setAlias(mainView.getOi().getAlias() + "_" + subView.getOi().getAlias());
        connector.setFromFieldName("alias");
        connector.setFromOIAlias(mainView.getOi().getAlias());
        connector.setToFieldName("userAlias");
        connector.setToOIAlias(subView.getOi().getAlias());

        List<ConnectorTree> connectorTreeList = new ArrayList<ConnectorTree>();
        connectorTreeList.add(ConnectorTreeUtil.buildConnectorTree(subView, connector));
        ConnectorTree queryTree = ConnectorTreeUtil.buildConnectorTree(mainView, connector, connectorTreeList, ConT.Left);


        DataPage dataPage = dataManager.page(1, 10, queryTree);
        System.out.println("Data -> " + JSONObject.toJSONString(dataPage));
    }


}
