package com.mokelay.db;

import com.mokelay.DBBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.database.mysql.manager.MysqlDataManager;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lego Transfer
 */
public class LegoTransferTest extends DBBaseTest {
    public void testTransfer() throws DBException, IOException, InstantiationException, IllegalAccessException {
        OI oi = new OI();
        oi.setAlias("ty_lego");
        oi.setResource("ty_lego");
        oi.setDsAlias("db_ty");
        oi.setName("乐高");
        oi.setId(8);

        //字段列表
        List<Field> fields = new ArrayList<Field>();
        String folder = TYPPC.getTYProp("mokelay.config.dev_dsl");
        // 读取YAML文件
        InputStream inputStream = new FileInputStream(folder + "/lego_fields.yaml");
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);
        for (Map f : data) {
            System.out.println(f);
            fields.add(mapToClass(f, Field.class));
        }

//        //条件
//        java.util.List<Condition> conList = new ArrayList<Condition>();
//        Condition c = new Condition();
//        c.setConditionFieldName(conn.getFromFieldName());
//        c.setConditionFieldValue(data.getString(aliaskey));
//        c.setCt(CT.EQ.getName());
//        conList.add(c);
//        MultiCondition condition = new MultiCondition(conList);

        MysqlDataManager mysqlDataManager = (MysqlDataManager) context.getBean("mysqlDataManager");
        List<Map> legoList = mysqlDataManager.list(oi, fields, null).getList();
        System.out.println(legoList);

        Representer representer = new Representer();
        representer.addClassTag(APIView.class, Tag.MAP);
        // 配置YAML生成选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
        options.setPrettyFlow(true); // 生成更易读的YAML

        for (Map d : legoList) {
            Yaml legoYaml = new Yaml(representer, options);
            legoYaml.dump(d, new FileWriter(folder + "/lego/" + d.get("alias") + ".yaml"));
        }
    }

    public static <T> T mapToClass(Map<String, Object> map, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // 允许访问私有字段
            Object value = map.get(field.getName());
            if (value != null) {
                field.set(instance, value);
            }
        }
        return instance;
    }
}
