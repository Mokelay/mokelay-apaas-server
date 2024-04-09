package com.greatbee.core.manager.ext;

import com.greatbee.core.manager.*;
import com.greatbee.db.manager.ConnectorManager;
import com.greatbee.db.manager.DSManager;
import com.greatbee.db.manager.FieldManager;
import com.greatbee.db.manager.OIManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simple TY Driver
 * <p>
 * Created by CarlChen on 2017/5/24.
 */
@Component("tyDriver")
public class SimpleTYDriver implements TYDriver {
    @Autowired
    private ConnectorManager connectorManager;
    @Autowired
    private DSManager dsManager;
    @Autowired
    private FileStorageManager fileStorageManager;
    @Autowired
    private LogManager logManager;
    @Autowired
    private TYCacheService tyCacheService;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskLogManager taskLogManager;


    @Override
    public DSManager getDsManager() {
        return dsManager;
    }

    @Override
    public ConnectorManager getConnectorManager() {
        return connectorManager;
    }

    @Override
    public FileStorageManager getFileStorageManager() {
        return fileStorageManager;
    }


    @Override
    public LogManager getLogManager() {
        return logManager;
    }

    @Override
    public TYCacheService getTyCacheService() {
        return tyCacheService;
    }

    @Override
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public TaskLogManager getTaskLogManager() {
        return taskLogManager;
    }
}
