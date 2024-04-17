package com.mokelay.db;

import com.mokelay.DBBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.server.API;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.core.manager.APIManager;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.core.service.APIContentService;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * API Transfer Test
 */
public class APITransferTest extends DBBaseTest {
    /**
     * Test Transfer
     */
    public void testTransfer() {
        APIManager apiManager = (APIManager) context.getBean("apiManager");
        TYDriver tyDriver = (TYDriver) context.getBean("tyDriver");

        try {
            List<API> apiList = apiManager.list();
            System.out.println("需要迁移API数量：" + apiList.size());
            if (CollectionUtil.isValid(apiList)) {
                //存储地址
                String apiFolder = TYPPC.getTYProp("mokelay.config.dsl") + "/api/";

                Representer representer = new Representer();
                representer.addClassTag(APIView.class, Tag.MAP);

                // 配置YAML生成选项
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
                options.setPrettyFlow(true); // 生成更易读的YAML

                for (API api : apiList) {
                    File directory = new File(apiFolder);

                    // 检查文件夹是否存在
                    if (!directory.exists()) {
                        // 创建文件夹
                        boolean result = directory.mkdirs();
                        if (result) {
                            System.out.println("Directory created: " + apiFolder);
                        } else {
                            System.out.println("Failed to create directory:" + apiFolder);
                        }
                    }

                    Yaml yaml = new Yaml(representer, options);
                    // 写入文件
                    try {
                        FileWriter writer = new FileWriter(apiFolder + api.getAlias() + ".yaml");

                        APIView apiView = APIContentService.buildAPIView(tyDriver.getTyCacheService(), api.getAlias());
                        apiView.getApi().setContent(null);
                        yaml.dump(apiView, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test Read API
     */
    public void testReadAPI() throws FileNotFoundException {
        String folder = TYPPC.getTYProp("mokelay.config.dsl") + "/api";

        InputStream inputStream = new FileInputStream(folder + "/ty/add-ad.yaml");

        // 将YAML内容转换为Java对象
        Yaml yaml = new Yaml(new Constructor(Map.class));
        APIView data = yaml.loadAs(inputStream, APIView.class);
        ;
        System.out.println(data);
    }
}
