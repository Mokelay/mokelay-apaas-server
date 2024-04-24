package com.mokelay.db;

import com.mokelay.DBBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.core.manager.YamlDSViewManager;
import com.mokelay.core.service.APIContentService;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.manager.ConnectorManager;
import com.mokelay.db.manager.DSManager;
import com.mokelay.db.manager.FieldManager;
import com.mokelay.db.manager.OIManager;
import org.springframework.core.io.FileSystemResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DSTransferTest extends DBBaseTest {
    public void testTransfer() throws DBException {
        DSManager dsManager = (DSManager) context.getBean("dsManager");
        OIManager oiManager = (OIManager) context.getBean("oiManager");
        FieldManager fieldManager = (FieldManager) context.getBean("fieldManager");
        ConnectorManager connectorManager = (ConnectorManager) context.getBean("connectorManager");

        Representer representer = new Representer();
        representer.addClassTag(DSView.class, Tag.MAP);

        // 配置YAML生成选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
        options.setPrettyFlow(true); // 生成更易读的YAML


        List<DS> dsList = dsManager.list();
        if (CollectionUtil.isValid(dsList)) {
            for (DS ds : dsList) {
                DSView dsView = new DSView();
                dsView.setDs(ds);

                //处理OI
                List<OI> ois = oiManager.list("dsAlias", ds.getAlias());
                if (CollectionUtil.isValid(ois)) {
                    List<OIView> oiViews = new ArrayList<>();
                    for (OI oi : ois) {
                        OIView oiView = new OIView();
                        oiView.setOi(oi);

                        //处理Fields
                        List<Field> fields = fieldManager.getFields(oi.getAlias());
                        oiView.setFields(fields);

                        oiViews.add(oiView);
                    }

                    dsView.setOiViews(oiViews);
                }

                //处理Connectors
                //这里Connector数据已经做了清理，都是TY数据库的connector
                dsView.setConnectors(connectorManager.list());

                Yaml yaml = new Yaml(representer, options);
                // 写入文件
                try {
                    //存储地址
                    String dsFolder = YamlBasicManager.DEFAULT_Mokelay_DS + "/ds/";
                    File f = new FileSystemResource(dsFolder + ds.getAlias() + ".yaml").getFile();
                    FileWriter writer = new FileWriter(f);
                    yaml.dump(dsView, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testGetDSByAlias() throws DBException {
        YamlDSViewManager dsViewManager = (YamlDSViewManager) context.getBean("dsViewManager");
        DSView dsView = dsViewManager.getDSViewByAlias("db_ty");
        System.out.println(dsView);
    }
}
