package com.mokelay.core.manager.ext;

import com.mokelay.base.manager.ext.AbstractBasicManager;
import com.mokelay.core.manager.TaskManager;
import com.mokelay.core.bean.task.Task;

/**
 * Created by ty on 2017/12/10.
 */
public class SimpleTaskManager extends AbstractBasicManager implements TaskManager {
    public SimpleTaskManager() {
        super(Task.class);
    }
}
