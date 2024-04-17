package com.mokelay.db.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.db.bean.oi.Connector;

import java.util.List;

/**
 * Created by CarlChen on 2016/11/11.
 */
public interface ConnectorManager extends BasicManager {
    /**
     * 通过ALias获取Connector
     * @param alias
     * @return
     * @throws DBException
     */
    public Connector getConnectorByAlias(String alias) throws DBException;
}
