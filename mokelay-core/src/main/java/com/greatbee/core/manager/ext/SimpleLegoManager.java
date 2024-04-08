package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.Lego;
import com.greatbee.core.manager.LegoManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleLegoManager extends AbstractBasicManager implements LegoManager {
    public SimpleLegoManager() {
        super(Lego.class);
    }
}
