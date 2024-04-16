package com.mokelay.core.manager.ext;

import com.mokelay.MokelayBaseTest;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.manager.TaskManager;
import junit.framework.TestCase;

import java.util.List;

public class YamlTaskManagerTest extends MokelayBaseTest {

    public void testList() {
        TaskManager taskManager = (TaskManager) context.getBean("taskManager");
        List<Task> tasks = taskManager.list();
        if (CollectionUtil.isValid(tasks)) {
            for (Task t : tasks) {
                System.out.println(t.getAlias());
            }
        }
    }
}