package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.LegoAttribute;
import com.greatbee.core.manager.LegoAttributeManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleLegoAttributeManager extends AbstractBasicManager implements LegoAttributeManager {
    public SimpleLegoAttributeManager() {
        super(LegoAttribute.class);
    }
}
