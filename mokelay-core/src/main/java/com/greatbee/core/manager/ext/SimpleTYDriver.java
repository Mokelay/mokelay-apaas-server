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
    private APILegoManager apiLegoManager;
    @Autowired
    private APIManager apiManager;
    @Autowired
    private ConnectorManager connectorManager;
    @Autowired
    private DSManager dsManager;
    @Autowired
    private OIManager oiManager;
    @Autowired
    private OutputFieldDescribeManager outputFieldDescribeManager;
    @Autowired
    private OutputFieldManager outputFieldManager;
    @Autowired
    private FieldManager fieldManager;
    @Autowired
    private InputFieldDescribeManager inputFieldDescribeManager;
    @Autowired
    private InputFieldManager inputFieldManager;
    @Autowired
    private LegoAttributeManager legoAttributeManager;
    @Autowired
    private LegoManager legoManager;
    @Autowired
    private FileStorageManager fileStorageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private APPManager appManager;
    @Autowired
    private APPRoleManager appRoleManager;
    @Autowired
    private UserAppRoleManager userAppRoleManager;
    @Autowired
    private PageManager pageManager;
    @Autowired
    private LogManager logManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private TYCacheService tyCacheService;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskLogManager taskLogManager;
    @Autowired
    private AuthTypeManager authTypeManager;

    @Override
    public APILegoManager getApiLegoManager() {
        return apiLegoManager;
    }
    @Override
    public APIManager getApiManager() {
        return apiManager;
    }
    @Override
    public DSManager getDsManager() {
        return dsManager;
    }
    @Override
    public OIManager getOiManager() {
        return oiManager;
    }
    @Override
    public ConnectorManager getConnectorManager() {
        return connectorManager;
    }

    @Override
    public OutputFieldDescribeManager getOutputFieldDescribeManager() {
        return outputFieldDescribeManager;
    }

    @Override
    public OutputFieldManager getOutputFieldManager() {
        return outputFieldManager;
    }

    @Override
    public FileStorageManager getFileStorageManager() {
        return fileStorageManager;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    @Override
    public InputFieldDescribeManager getInputFieldDescribeManager() {
        return inputFieldDescribeManager;
    }

    @Override
    public InputFieldManager getInputFieldManager() {
        return inputFieldManager;
    }

    @Override
    public LegoAttributeManager getLegoAttributeManager() {
        return legoAttributeManager;
    }

    @Override
    public LegoManager getLegoManager() {
        return legoManager;
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public APPManager getAppManager() {
        return appManager;
    }

    @Override
    public APPRoleManager getAppRoleManager() {
        return appRoleManager;
    }

    @Override
    public UserAppRoleManager getUserAppRoleManager() {
        return userAppRoleManager;
    }

    @Override
    public PageManager getPageManager() {
        return pageManager;
    }

    @Override
    public LogManager getLogManager() {
        return logManager;
    }

    @Override
    public CodeManager getCodeManager() {
        return codeManager;
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

    @Override
    public AuthTypeManager getAuthTypeManager() {
        return authTypeManager;
    }
}
