package com.mokelay.core.manager.ext;

import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.manager.TaskManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("taskManager")
public class YamlTaskManager extends YamlBasicManager implements TaskManager {

    public YamlTaskManager() {
        super(Task.class, DEFAULT_Mokelay_DS, "/task/task.yaml");
    }
}
