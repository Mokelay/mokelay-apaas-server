package com.mokelay.core.manager.ext;

import com.mokelay.base.util.ObjectUtil;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.core.manager.TaskManager;
import com.mokelay.db.bean.oi.Field;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("taskManager")
public class YamlTaskManager implements TaskManager {
    /**
     * List All Tasks
     *
     * @return
     */
    public List<Task> list() {
        List<Task> tasks = new ArrayList<Task>();
        String folder = TYPPC.getTYProp("mokelay.config.dsl");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(folder + "/task/task.yaml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);

        for (Map f : data) {
            System.out.println(f);
            tasks.add(ObjectUtil.mapToClass(f, Task.class));
        }
        return tasks;
    }
}
