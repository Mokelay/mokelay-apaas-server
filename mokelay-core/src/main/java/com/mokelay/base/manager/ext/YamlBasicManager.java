package com.mokelay.base.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataPage;
import com.mokelay.base.bean.Identified;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.base.util.ObjectUtil;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.OI;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * YAML Basic Manager
 */
public abstract class YamlBasicManager<T extends Identified> implements BasicManager {
    public static final String DEFAULT_Mokelay_DS = TYPPC.getTYProp("mokelay.config.dsl");

    protected Class<T> beanClass;
    protected String connectionUrl;
    protected String resource;

    public YamlBasicManager(Class<T> beanClass, DS ds, OI oi) {
        this.beanClass = beanClass;
        this.connectionUrl = ds.getConnectionUrl();
        this.resource = oi.getResource();
    }

    public YamlBasicManager(Class<T> beanClass, String connectionUrl, String resource) {
        this.beanClass = beanClass;
        this.connectionUrl = connectionUrl;
        this.resource = resource;
    }

    @Override
    public int add(Object object) throws DBException {
        return 0;
    }

    @Override
    public int add(List<Object> bean) throws DBException {
        return 0;
    }

    @Override
    public void update(Object object) throws DBException {

    }

    @Override
    public void update(int id, String columnName, Object columnValue) throws DBException {

    }

    @Override
    public Object read(int id) throws DBException {
        return null;
    }

    @Override
    public List read(int[] ids) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public List list() throws DBException {
        String path = connectionUrl + resource;
        File f = new File(path);
        if (f.isDirectory()) {
            File[] listOfFiles = f.listFiles();

            List objs = new ArrayList();
            if (listOfFiles != null) {

                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        System.out.println("File: " + file.getName());

                        if (file.getName().endsWith(".yaml")) {
                            InputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(path + "/" + file.getName());
                            } catch (FileNotFoundException e) {
                                throw new DBException(e.getMessage(), e, 20000004);
                            }

                            // 将YAML内容转换为Java对象
                            Yaml yaml = new Yaml(new Constructor(Map.class));
                            Object data = yaml.loadAs(inputStream, beanClass);
                            objs.add(data);
                        }
                    } else if (file.isDirectory()) {
                        System.out.println("Directory: " + file.getName());
                    }
                }
            } else {
                System.out.println("The directory is empty or does not exist.");
            }
            System.out.println("List NUMS:" + objs.size());
            return objs;
        } else {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                throw new DBException(e.getMessage(), e, 20000004);
            }

            // 创建Yaml实例
            Yaml yaml = new Yaml(new Constructor(List.class));
            // 将YAML内容转换为Java对象
            List<Map> data = yaml.load(inputStream);

            List<T> objs = new ArrayList<T>();
            for (Map d : data) {
                objs.add((T) ObjectUtil.mapToClass(d, beanClass));
            }
            return objs;
        }
    }

    @Override
    public List list(String columnName, Object columnValue) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public List list(String columnName, Object columnValue, String orderColumn, boolean asc) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public List list(String orderColumn, boolean asc) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public List list(Condition condition) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public List list(Condition condition, String orderColumn, boolean asc) throws DBException {
        return Collections.emptyList();
    }

    @Override
    public DataPage page(int page, int pageSize) throws DBException {
        return page(page, page, null);
    }

    @Override
    public DataPage page(int page, int pageSize, Condition condition) throws DBException {
        return page(page, pageSize, condition, null, false);
    }

    @Override
    public DataPage page(int page, int pageSize, Condition condition, String orderColumn, boolean isAsc) throws DBException {
        return null;
    }

    @Override
    public void delete(int id) throws DBException {

    }

    @Override
    public void delete(int[] operationBeanIds) throws DBException {

    }
}
