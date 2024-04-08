package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.client.Page;
import com.greatbee.core.manager.PageManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimplePageManager extends AbstractBasicManager implements PageManager {
    public SimplePageManager() {
        super(Page.class);
    }
}
