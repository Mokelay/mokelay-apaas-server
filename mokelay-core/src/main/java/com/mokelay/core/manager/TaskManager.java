package com.mokelay.core.manager;

import com.mokelay.core.bean.task.Task;

import java.util.List;

/**
 * Task Manager
 * <p>
 * Created by ty on 2017/12/10.
 */
public interface TaskManager {
    /**
     * List Tasks
     *
     * @return
     */
    public List<Task> list();
}
