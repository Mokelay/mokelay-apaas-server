package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.manager.TaskLogManager;
import com.greatbee.core.bean.task.TaskLog;

/**
 * Created by ty on 2017/12/10.
 */
public class SimpleTaskLogManager extends AbstractBasicManager implements TaskLogManager {
    public SimpleTaskLogManager() {
        super(TaskLog.class);
    }
}
