package com.mokelay.core.manager;

import com.mokelay.db.manager.ConnectorManager;
import com.mokelay.db.manager.DSManager;

/**
 * TY Driver
 * <p>
 * Created by CarlChen on 16/10/11.
 */
public interface TYDriver {
    /**
     * Connector Manager
     *
     * @return
     */
    public ConnectorManager getConnectorManager();

    /**
     * DSManager
     *
     * @return
     */
    public DSManager getDsManager();

    /**
     * FileStorageManager Manager
     *
     * @return
     */
    public FileStorageManager getFileStorageManager();

    /**
     * TY Cache Service
     * @return
     */
    TYCacheService getTyCacheService();
}
