package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.bean.auth.AuthType;
import com.greatbee.db.bean.oi.Connector;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.core.bean.server.API;
import com.greatbee.api.bean.server.APILego;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.lego.util.BuildConnectorTreeUtils;
import com.greatbee.db.manager.ConnectorManager;
import com.greatbee.db.manager.DSManager;
import com.greatbee.db.manager.FieldManager;
import com.greatbee.db.manager.OIManager;
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
    private APILegoManager apiLegoManager;
    @Autowired
    private APIManager apiManager;
    @Autowired
    private ConnectorManager connectorManager;
    @Autowired
    private FieldManager fieldManager;
    @Autowired
    private InputFieldManager inputFieldManager;
    @Autowired
    private OIManager oiManager;
    @Autowired
    private OutputFieldManager outputFieldManager;
    @Autowired
    private DSManager dsManager;
    @Autowired
    private AuthTypeManager authTypeManager;

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
            @CacheEvict(value="authType",beforeInvocation = true,allEntries = true)
    })
    public void clearAllCache(){
        //清空oi view 缓存
        BuildConnectorTreeUtils.clearOICache();
        return;
    }

    @Cacheable(value = "apiLego")
    public List<APILego> getAPILegoList(String apiAlias) throws DBException{
        return apiLegoManager.getAPILegoList(apiAlias);
    }

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

    @Cacheable(value = "inputField")
    public List<InputField> getInputFields(String apiLegoUuid) throws DBException{
        return inputFieldManager.getInputFields(apiLegoUuid);
    }

    @Cacheable(value = "oi")
    public OI getOIByAlias(String alias) throws DBException{
        return oiManager.getOIByAlias(alias);
    }

    @Cacheable(value = "outputField")
    public List<OutputField> getOutputFields(String apiLegoUuid) throws DBException{
        return outputFieldManager.getOutputFields(apiLegoUuid);
    }

    @Cacheable(value = "ds")
    public DS getDSByAlias(String alias) throws DBException{
        return dsManager.getDSByAlias(alias);
    }
    @Cacheable(value = "authType")
    public AuthType getAuthTypeByAlias(String alias) throws DBException {
        return authTypeManager.getAuthTypeByAlias(alias);
    }

}
