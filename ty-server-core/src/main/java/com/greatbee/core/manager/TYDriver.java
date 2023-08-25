package com.greatbee.core.manager;

import com.greatbee.core.service.APIContentService;

/**
 * TY Driver
 * <p>
 * Created by CarlChen on 16/10/11.
 */
public interface TYDriver {
    /**
     * API Lego Manager
     *
     * @return
     */
    public APILegoManager getApiLegoManager();

    /**
     * API Manager
     *
     * @return
     */
    public APIManager getApiManager();

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
     * FieldManager
     *
     * @return
     */
    public FieldManager getFieldManager();

    /**
     * InputFieldDEscribe Manager
     *
     * @return
     */
    public InputFieldDescribeManager getInputFieldDescribeManager();

    /**
     * InputFieldManager
     *
     * @return
     */
    public InputFieldManager getInputFieldManager();

    /**
     * LegoAttribute Manager
     *
     * @return
     */
    public LegoAttributeManager getLegoAttributeManager();

    /**
     * Lego Manager
     *
     * @return
     */
    public LegoManager getLegoManager();

    /**
     * OIManager
     *
     * @return
     */
    public OIManager getOiManager();

    /**
     * OutputFieldDescribe Manager
     *
     * @return
     */
    public OutputFieldDescribeManager getOutputFieldDescribeManager();

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

    UserManager getUserManager();

    APPManager getAppManager();

    APPRoleManager getAppRoleManager();

    UserAppRoleManager getUserAppRoleManager();

    PageManager getPageManager();

    LogManager getLogManager();

    CodeManager getCodeManager();

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

    /**
     * Auth Type Manager
     *
     * @return
     */
    public AuthTypeManager getAuthTypeManager();
}
