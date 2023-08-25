package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.auth.AuthType;
import com.greatbee.core.manager.AuthTypeManager;

import java.util.List;

/**
 * Auth Type Manager
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/14
 */
public class SimpleAuthTypeManager extends AbstractBasicManager implements AuthTypeManager {
    public SimpleAuthTypeManager() {
        super(AuthType.class);
    }

    @Override
    public AuthType getAuthTypeByAlias(String alias) throws DBException {
        List<AuthType> dsList = this.list("alias", alias);
        if (CollectionUtil.isValid(dsList)) {
            return dsList.get(0);
        } else {
            return null;
        }
    }
}
