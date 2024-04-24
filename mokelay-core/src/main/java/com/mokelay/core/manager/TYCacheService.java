package com.mokelay.core.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.core.bean.auth.AuthType;
import com.mokelay.core.bean.task.Task;
import com.mokelay.db.bean.oi.Connector;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.core.bean.server.API;
import com.mokelay.core.lego.util.BuildConnectorTreeUtils;
import com.mokelay.db.manager.ConnectorManager;
import com.mokelay.db.manager.DSManager;
import com.mokelay.db.manager.FieldManager;
import com.mokelay.db.manager.OIManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TY Cache Service
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/5
 */
@Component("tyCacheService")
public class TYCacheService {
    @Autowired
    private APIManager apiManager;
    @Autowired
    private ConnectorManager connectorManager;
    @Autowired
    private FieldManager fieldManager;
    @Autowired
    private OIManager oiManager;
    @Autowired
    private DSManager dsManager;
    @Autowired
    private AuthTypeManager authTypeManager;
    @Autowired
    private TaskManager taskManager;

    /**
     * 清除所有TY cache缓存
     */
    @Caching(evict = {
            @CacheEvict(value="api",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="apiLego",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="inputField",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="outputField",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="connector",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="fields",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="oi",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="ds",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="authType",beforeInvocation = true,allEntries = true),
            @CacheEvict(value="tasks",beforeInvocation = true,allEntries = true)
    })
    public void clearAllCache(){
        //清空oi view 缓存
        BuildConnectorTreeUtils.clearOICache();
        return;
    }

//    @Cacheable(value = "apiLego")
//    public List<APILego> getAPILegoList(String apiAlias) throws DBException{
//        return apiLegoManager.getAPILegoList(apiAlias);
//    }

    @Cacheable(value = "api")
    public API getAPIByAlias(String alias) throws DBException{
        return apiManager.getAPIByAlias(alias);
    }

    @Cacheable(value = "connector")
    public Connector getConnectorByAlias(String alias) throws DBException{
        return connectorManager.getConnectorByAlias(alias);
    }

    @Cacheable(value = "fields")
    public List<Field> getFields(String oiAlias) throws DBException{
        return fieldManager.getFields(oiAlias);
    }

//    @Cacheable(value = "inputField")
//    public List<InputField> getInputFields(String apiLegoUuid) throws DBException{
//        return inputFieldManager.getInputFields(apiLegoUuid);
//    }

    @Cacheable(value = "oi")
    public OI getOIByAlias(String alias) throws DBException{
        return oiManager.getOIByAlias(alias);
    }

    @Cacheable(value = "ds")
    public DS getDSByAlias(String alias) throws DBException{
        return dsManager.getDSByAlias(alias);
    }
    @Cacheable(value = "authType")
    public AuthType getAuthTypeByAlias(String alias) throws DBException {
        return authTypeManager.getAuthTypeByAlias(alias);
    }

    @Cacheable(value = "tasks")
    public List<Task> listTasks() throws DBException {
        return taskManager.list();
    }
}
