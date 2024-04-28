package com.mokelay.db.database.yaml;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.bean.DataPage;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.base.manager.ext.YamlManager;
import com.mokelay.core.manager.YamlDSViewManager;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.database.DataManager;
import com.mokelay.db.database.base.BaseTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component("yamlDataManager")
public class YamlDataManager extends YamlManager implements DataManager {
    @Autowired
    private YamlDSViewManager dsViewManager;

    @Override
    public Data read(OI oi, List<Field> fields, Field pkField) throws DBException {
        return null;
    }

    @Override
    public DataPage page(OI oi, List<Field> fields, int page, int pageSize, Condition condition) throws DBException {
        return null;
    }

    @Override
    public DataList list(OI oi, List<Field> fields, Condition condition) throws DBException {
        return null;
    }

    @Override
    public void delete(OI oi, Field pkField) throws DBException {

    }

    @Override
    public void delete(OI oi, Condition condition) throws DBException {

    }

    @Override
    public String create(OI oi, List<Field> fields) throws DBException {
        return "";
    }

    public void batchCreate(OI oi, List dataList) throws DBException {
        // Representer representer = new Representer();
        // representer.addClassTag(APIView.class, Tag.MAP);
        // 配置YAML生成选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块风格
        options.setPrettyFlow(true); // 生成更易读的YAML

        Yaml legoYaml = new Yaml(options);

        File address = _getYamlBeanAddress(oi);
        try {
            File f = new FileSystemResource(new File(address,oi.getAlias() + ".yaml")).getFile();
            legoYaml.dump(dataList, new FileWriter(f));
        } catch (IOException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    private File _getYamlBeanAddress(OI oi) {
        String dsAlias = oi.getDsAlias();
        DSView dsView = null;
        try {
            dsView = dsViewManager.getDSViewByAlias(dsAlias);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
        return getYamlBeanAddress(dsView.getDs().getConnectionUrl(), oi.getResource());
    }

    @Override
    public void update(OI oi, List<Field> fields, Field pkField) throws DBException {

    }

    @Override
    public void update(OI oi, List<Field> fields, Condition condition) throws DBException {

    }

    @Override
    public void executeTransaction(DS ds, List<BaseTransactionTemplate> transactionNodes) throws DBException {

    }
}
