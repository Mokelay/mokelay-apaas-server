package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.user.AppRole;
import com.greatbee.core.manager.APPRoleManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleAppRoleManager extends AbstractBasicManager implements APPRoleManager {
    public SimpleAppRoleManager() {
        super(AppRole.class);
    }
}
