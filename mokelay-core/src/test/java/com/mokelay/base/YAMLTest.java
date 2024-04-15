package com.mokelay.base;

import com.mokelay.MokelayBaseTest;
import com.mokelay.core.lego.system.TYPPC;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.*;

/**
 * YAML Test
 */
public class YAMLTest extends MokelayBaseTest {
    /**
     * Test Write YAML
     */
    public void testWriteYAML() {
        // 准备数据
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "John Doe");
        data.put("age", 30);
        data.put("married", true);
        data.put("children", Arrays.asList("Jane", "Mark"));

        Map<String, Object> address = new HashMap<>();
        address.put("street", "1234 Elm Street");
        address.put("city", "SomeCity");
        address.put("zip", "123456");

        data.put("address", address);

        Map<String, Object> data2 = new HashMap<String, Object>();
        data2.put("name", "AAAA");
        data2.put("age", 444);
        data2.put("married", false);
        data2.put("children", Arrays.asList("DDDD", "EEE"));

        Map<String, Object> address2 = new HashMap<>();
        address2.put("street", "CHINA");
        address2.put("city", "SHANGHAI");
        address2.put("zip", "200001");

        data2.put("address", address2);

        // 配置YAML生成选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
        options.setPrettyFlow(true); // 生成更易读的YAML

        String folder = TYPPC.getTYProp("mokelay.config.dsl");
//        System.out.println(folder);
        // 创建Yaml实例
        Yaml yaml = new Yaml(options);

        // 写入文件
        try {
            FileWriter writer = new FileWriter(folder + "/output.yaml");

            List l = new ArrayList();
            l.add(data);
            l.add(data2);
            yaml.dump(l, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Read YAML
     *
     */
    public void testReadYAML() {
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(Map.class));

        try {
            String folder = TYPPC.getTYProp("mokelay.config.dsl");
            // 读取YAML文件
            InputStream inputStream = new FileInputStream(folder + "/output.yaml");

            // 将YAML内容转换为Java对象
            Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);

            // 打印结果，验证内容
            System.out.println(data);

            // 示例：访问转换后的Map中的数据
            System.out.println("Name: " + data.get("name"));
            System.out.println("Age: " + data.get("age"));

            if (data.containsKey("address")) {
                Map<String, Object> address = (Map<String, Object>) data.get("address");
                System.out.println("City: " + address.get("city"));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
