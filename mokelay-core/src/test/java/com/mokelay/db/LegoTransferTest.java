package com.mokelay.db;

import com.mokelay.DBBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.constant.CT;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.base.util.ObjectUtil;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.bean.view.MultiCondition;
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
    /**
     * 导出所有的Lego
     *
     * @throws DBException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void testTransfer() throws DBException, IOException, InstantiationException, IllegalAccessException {
        //配置文件目录
        String folder = TYPPC.getTYProp("mokelay.config.dev_dsl");

        Representer representer = new Representer();
        representer.addClassTag(APIView.class, Tag.MAP);
        // 配置YAML生成选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
        options.setPrettyFlow(true); // 生成更易读的YAML

        List<Map> legoList = _getLegoList(folder);
        for (Map lego : legoList) {
            //取出Input
            lego.put("inputFieldDescribe ", _getInputFieldDescribe(folder, lego));

            //取出Ouput
            lego.put("outputFieldDescribe", _getOutputFieldDescribe(folder, lego));

            Yaml legoYaml = new Yaml(representer, options);
            legoYaml.dump(lego, new FileWriter(folder + "/lego/" + lego.get("alias") + ".yaml"));
        }
    }

    /**
     * 取出所有的Lego
     *
     * @return
     * @throws FileNotFoundException
     */
    private List<Map> _getLegoList(String folder) throws FileNotFoundException, InstantiationException, IllegalAccessException, DBException {
        OI oi = new OI();
        oi.setAlias("ty_lego");
        oi.setResource("ty_lego");
        oi.setDsAlias("db_ty");
        oi.setName("乐高");
        oi.setId(8);

        //字段列表
        List<Field> fields = new ArrayList<Field>();
        // 读取YAML文件
        InputStream inputStream = new FileInputStream(folder + "/lego/lego_oi/lego.yaml");
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);
        for (Map f : data) {
            System.out.println(f);
            fields.add(ObjectUtil.mapToClass(f, Field.class));
        }
        MysqlDataManager mysqlDataManager = (MysqlDataManager) context.getBean("mysqlDataManager");
        return mysqlDataManager.list(oi, fields, null).getList();
    }

    /**
     * 取出所有的输入字段描述
     *
     * @param folder
     * @param lego
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws FileNotFoundException
     * @throws DBException
     */
    private List<Map> _getInputFieldDescribe(String folder, Map lego) throws InstantiationException, IllegalAccessException, FileNotFoundException, DBException {
        MysqlDataManager mysqlDataManager = (MysqlDataManager) context.getBean("mysqlDataManager");
        OI oi = new OI();
        oi.setAlias("ty_input_field_describe");
        oi.setResource("ty_input_field_describe");
        oi.setDsAlias("db_ty");
        oi.setName("输入字段描述");
        oi.setId(7);

        //字段列表
        List<Field> fields = new ArrayList<Field>();
        // 读取YAML文件
        InputStream inputStream = new FileInputStream(folder + "/lego/lego_oi/input_field_describe.yaml");
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);
        for (Map f : data) {
            System.out.println(f);
            fields.add(ObjectUtil.mapToClass(f, Field.class));
        }

        //条件
        java.util.List<Condition> conList = new ArrayList<Condition>();
        Condition c = new Condition();
        c.setConditionFieldName("legoAlias");
        c.setConditionFieldValue((String) lego.get("alias"));
        c.setCt(CT.EQ.getName());
        conList.add(c);
        MultiCondition condition = new MultiCondition(conList);

        return mysqlDataManager.list(oi, fields, condition).getList();
    }

    private List<Map> _getOutputFieldDescribe(String folder, Map lego) throws InstantiationException, IllegalAccessException, FileNotFoundException, DBException {
        MysqlDataManager mysqlDataManager = (MysqlDataManager) context.getBean("mysqlDataManager");
        OI oi = new OI();
        oi.setAlias("ty_output_field_describe");
        oi.setResource("ty_output_field_describe");
        oi.setDsAlias("db_ty");
        oi.setName("输出字段描述");
        oi.setId(12);

        //字段列表
        List<Field> fields = new ArrayList<Field>();
        // 读取YAML文件
        InputStream inputStream = new FileInputStream(folder + "/lego/lego_oi/output_field_describe.yaml");
        // 创建Yaml实例
        Yaml yaml = new Yaml(new Constructor(List.class));
        // 将YAML内容转换为Java对象
        List<Map> data = yaml.load(inputStream);
        for (Map f : data) {
            System.out.println(f);
            fields.add(ObjectUtil.mapToClass(f, Field.class));
        }

        //条件
        java.util.List<Condition> conList = new ArrayList<Condition>();
        Condition c = new Condition();
        c.setConditionFieldName("legoAlias");
        c.setConditionFieldValue((String) lego.get("alias"));
        c.setCt(CT.EQ.getName());
        conList.add(c);
        MultiCondition condition = new MultiCondition(conList);

        return mysqlDataManager.list(oi, fields, condition).getList();
    }
}
