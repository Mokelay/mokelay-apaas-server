package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.user.UserAppRole;
import com.greatbee.core.manager.UserAppRoleManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleUserAppRoleManager extends AbstractBasicManager implements UserAppRoleManager {
    public SimpleUserAppRoleManager() {
        super(UserAppRole.class);
    }
}
