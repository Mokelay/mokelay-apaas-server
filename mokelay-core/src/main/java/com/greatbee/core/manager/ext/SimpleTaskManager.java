package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.manager.TaskManager;
import com.greatbee.core.bean.task.Task;

/**
 * Created by ty on 2017/12/10.
 */
public class SimpleTaskManager extends AbstractBasicManager implements TaskManager {
    public SimpleTaskManager() {
        super(Task.class);
    }
}
