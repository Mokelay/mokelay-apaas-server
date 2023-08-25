package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.user.Log;
import com.greatbee.core.manager.LogManager;

/**
 * Simple Log Manager
 * <p/>
 * Created by CarlChen on 2017/5/24.
 */
public class SimpleLogManager extends AbstractBasicManager implements LogManager {
    public SimpleLogManager() {
        super(Log.class);
    }


}
