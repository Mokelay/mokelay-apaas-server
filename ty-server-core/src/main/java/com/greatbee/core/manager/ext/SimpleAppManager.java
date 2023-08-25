package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.user.App;
import com.greatbee.core.manager.APPManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleAppManager extends AbstractBasicManager implements APPManager {
    public SimpleAppManager() {
        super(App.class);
    }
}
