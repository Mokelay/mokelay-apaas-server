package com.mokelay.core.manager.ext;

import com.mokelay.core.manager.*;
import com.mokelay.db.manager.ConnectorManager;
import com.mokelay.db.manager.DSManager;
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
    private TYCacheService tyCacheService;
    @Autowired
    private TaskManager taskManager;


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
    public TYCacheService getTyCacheService() {
        return tyCacheService;
    }
}
