package com.mokelay.base.manager;

import com.mokelay.base.util.ObjectUtil;
import com.mokelay.core.lego.system.TYPPC;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * YAML基础管理器
 */
public class BasicYAMLManager {

    /**
     * List YAML File
     *
     * @param clazz
     * @param yamlFile
     * @param <T>
     * @return
     */
    protected <T> List<T> _list(Class<T> clazz, String yamlFile) {
        String folder = TYPPC.getTYProp("mokelay.config.dsl");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(folder + yamlFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);


        List<T> objs = new ArrayList<T>();
        for (Map f : data) {
            objs.add((T) ObjectUtil.mapToClass(f, clazz));
        }

        return objs;
    }
}
