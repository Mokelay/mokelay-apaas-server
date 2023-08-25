package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.server.API;
import com.greatbee.core.manager.APIManager;

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
