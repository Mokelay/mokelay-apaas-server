package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.user.User;
import com.greatbee.core.manager.UserManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleUserManager extends AbstractBasicManager implements UserManager {
    public SimpleUserManager() {
        super(User.class);
    }
}
