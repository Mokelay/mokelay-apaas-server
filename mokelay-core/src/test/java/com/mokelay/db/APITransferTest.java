package com.mokelay.db;

import com.mokelay.DBBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.server.API;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.core.manager.APIManager;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.List;

/**
 * API Transfer Test
 */
public class APITransferTest extends DBBaseTest {
    /**
     * Test Transfer
     */
    public void testTransfer() {
        APIManager apiManager = (APIManager) context.getBean("apiManager");

        try {
            List<API> apiList = apiManager.list();
            System.out.println("需要迁移API数量：" + apiList.size());
            if (CollectionUtil.isValid(apiList)) {
                //存储地址
                String folder = TYPPC.getTYProp("mokelay.config.dsl") + "/api/";

                // 配置YAML生成选项
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
                options.setPrettyFlow(true); // 生成更易读的YAML

                for (API api : apiList) {
                    String apiTypeFolder = folder + api.getType();
                    File directory = new File(apiTypeFolder);

                    // 检查文件夹是否存在
                    if (!directory.exists()) {
                        // 创建文件夹
                        boolean result = directory.mkdirs();
                        if (result) {
                            System.out.println("Directory created: " + apiTypeFolder);
                        } else {
                            System.out.println("Failed to create directory:" + apiTypeFolder);
                        }
                    }

                    Yaml yaml = new Yaml(options);
                    // 写入文件
                    try {
                        FileWriter writer = new FileWriter(apiTypeFolder + "/" + api.getAlias() + ".yaml");
                        yaml.dump(api, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
