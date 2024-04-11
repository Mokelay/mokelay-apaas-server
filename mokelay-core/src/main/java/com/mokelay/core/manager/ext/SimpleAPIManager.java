package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.AbstractBasicManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.server.API;
import com.mokelay.core.manager.APIManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleAPIManager extends AbstractBasicManager implements APIManager {
    public SimpleAPIManager() {
        super(API.class);
    }

    @Override
    public API getAPIByAlias(String alias) throws DBException {
        List<API> apis = this.list("alias",alias);
        if(CollectionUtil.isValid(apis)){
            return apis.get(0);
        }
        return null;
    }
}
