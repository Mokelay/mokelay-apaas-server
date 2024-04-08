package com.greatbee.core.manager;

import com.greatbee.db.manager.ConnectorManager;
import com.greatbee.db.manager.DSManager;
import com.greatbee.db.manager.FieldManager;
import com.greatbee.db.manager.OIManager;

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
     * InputFieldManager
     *
     * @return
     */
    public InputFieldManager getInputFieldManager();

    /**
     * OutputFieldManager
     *
     * @return
     */
    public OutputFieldManager getOutputFieldManager();

    /**
     * FileStorageManager Manager
     *
     * @return
     */
    public FileStorageManager getFileStorageManager();

    /**
     * Log Manager
     *
     * @return
     */
    LogManager getLogManager();

    /**
     * TY Cache Service
     * @return
     */
    TYCacheService getTyCacheService();

    /**
     * Task Manager
     *
     * @return
     */
    public TaskManager getTaskManager();

    /**
     * Task Log Manager
     *
     * @return
     */
    public TaskLogManager getTaskLogManager();
}
