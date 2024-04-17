package com.mokelay.db.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.AbstractBasicManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.db.bean.oi.Connector;
import com.mokelay.db.manager.ConnectorManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleConnectorManager  extends AbstractBasicManager implements ConnectorManager {
    public SimpleConnectorManager() {
        super(Connector.class);
    }

    @Override
    public Connector getConnectorByAlias(String alias) throws DBException {
        List<Connector> cs = this.list("alias",alias);

        return CollectionUtil.isValid(cs)?cs.get(0):null;
    }
}
