package com.mokelay.core.manager.ext;

import com.mokelay.base.manager.BasicYAMLManager;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.manager.TaskManager;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("taskManager")
public class YamlTaskManager extends BasicYAMLManager implements TaskManager {
    /**
     * List All Tasks
     *
     * @return
     */
    public List<Task> list() {
        return _list(Task.class, "/task/task.yaml");
    }
}
