package com.mokelay.vendor.lego.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.constant.DBMT;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.db.bean.oi.Connector;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.db.database.DataManager;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.api.util.SpringContextUtil;
import com.mokelay.vendor.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

/**
 * 级联插入
 * 输入：1、Input_Key_Index_Data_Object 数据源，可以是list或者是DataList （List<List> 或者 List<String[]>）
 * 数据格式如下。例如：a:api表；b:apilego表；c:inputFIeld表；d:ouputField表
 * [
 * [a1,a2,b1,b2,c1,c2,d1,d2],
 * [null,null,null,null,c1,c2,d1,d2],
 * [null,null,null,null,c1,c2,d1,d2],
 * [null,null,b1,b2,c1,c2,d1,d2],
 * ...
 * ]
 * 把上面的格式数据转换成List<CascadeTree>
 * [{
 * connector:null,
 * oi:a,
 * fields:a1,a2,
 * children:[{
 * connector:b-a,
 * oi:b,
 * fields:b1,b2,
 * children:[{
 * connector:c-b,
 * oi:c,
 * fields:c1,c2,
 * children:[]
 * },{
 * connector:d-b,
 * oi:d,
 * fields:d1,d2,
 * children:[]
 * }...]
 * }...]
 * }]
 * <p>
 * 2、创建字段 通过反向树形级联选择，选择出需要插入的字段列，格式如下：
 * {
 * "name": "别名-UUID(ty_api_lego)-description(ty_output_field)",
 * "fieldName": "description",
 * "fieldValue" "",//fieldValue 用来存储其他值，比如固定值，或者当前登录人信息等
 * "alias": "1",  //alias用常量直接定义从数据的哪个下标取值，比如这里是1，取值方式为data.get(1)(list的形式) 或者 data[1](数组的形式)
 * "ift": "create",
 * "connectorPath": "[{\"alias\":\"apiLego_api\"},{\"alias\":\"of_apiLego\"}]",
 * }
 * 这个字段的查找路径是  api表的alias字段 -> apilego表的uuid字段 -> ouputField表 的descreption字段
 * <p>
 * 输出：Output_Key_unique_value_list 返回级联添加的唯一值列表
 *
 * @author xiaobc
 * @date 18/7/31
 */
@Component("cascadeAdd")
public class CascadeAdd implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(CascadeAdd.class);

    private static final String Input_Key_Index_Data_Object = "index_data";//数据源 可以是data 也可以是list 或DataList
    private static final String Output_Key_unique_value_list = "unique_value_list";//返回唯一值列表

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        ApplicationContext wac = SpringContextUtil.getApplicationContext();

        Object indexDataObj = input.getInputObjectValue(Input_Key_Index_Data_Object);
        if (indexDataObj == null) {
            throw new LegoException("缺少主数据", VendorExceptionCode.Lego_Error_Index_data_Null);
        }
        List indexData = null;
        if (indexDataObj instanceof DataList) {
            indexData = ((DataList) indexDataObj).getList();
        } else if (indexDataObj instanceof String) {
            JSONArray array = JSON.parseArray((String) indexDataObj);
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Object o = array.get(i);
                list.add(o);
            }
            indexData = list;
        } else {
            indexData = (List) indexDataObj;
        }

        String srcOiAlias = input.getApiLego().getOiAlias();

        //1、获取所有的添加字段  按下标顺序生成一个 Map<Integer,CascadeField>数据，可以直接通过下标index 获取到字段的详细信息，包括字段名，连接器等信息
        List<InputField> inputFields = input.getInputField(IOFT.Create);
        if (CollectionUtil.isInvalid(inputFields)) {
            throw new LegoException("没有有效的导入字段配置", VendorExceptionCode.Lego_Error_CascadeAdd_Create_Field_Null);
        }
        //记录每个下标的字段信息
        Map<Integer, CascadeField> map = buildCreateFields(srcOiAlias, inputFields);
        //记录模板中 每张表是哪几列
        Map<String, CascadeOi> oiMap = buildCascadeOi(map);

        //2、遍历index_data 生成List<CascadeTree>
        List<CascadeTree> cts = buildCascadeTree(indexData, oiMap, map);

        //3、根据CascadeTree插入数据
        List<String> uniqueValues = cascadeInsert(cts, wac);

        //4、返回插入的数据唯一值列表
        output.setOutputValue(Output_Key_unique_value_list, uniqueValues);
    }

    /**
     * 根据list<CascadeTree> 插入到数据库
     *
     * @param cts
     * @return
     */
    private List<String> cascadeInsert(List<CascadeTree> cts, ApplicationContext wac) throws LegoException {
        List<String> uniqueValues = new ArrayList<>();
        for (CascadeTree ct : cts) {
            List<String> uniques = insertCascadeTree(null, ct, wac);//根tree 没有父级，父级参数传null
            uniqueValues.addAll(uniques);
        }
        return uniqueValues;
    }

    /**
     * 递归将CascadeTree 插入到数据库中
     *
     * @param ct
     * @param wac
     * @return
     */
    private List<String> insertCascadeTree(CascadeTree parentTree, CascadeTree ct, ApplicationContext wac) throws LegoException {
        List<String> uniqueValues = new ArrayList<>();
        String unique = singleInsert(parentTree, ct, wac);
        //获取到unique值后存放到CascadeTree的uniqueValue值上
        ct.setUniqueValue(unique);

        uniqueValues.add(unique);
        if (CollectionUtil.isValid(ct.getChildren())) {
            for (CascadeTree childCt : ct.getChildren()) {
                List<String> uniqueChilds = insertCascadeTree(ct, childCt, wac);
                uniqueValues.addAll(uniqueChilds);
            }
        }
        return uniqueValues;
    }

    /**
     * 单条插入数据  返回唯一值
     *
     * @param parentTree
     * @param currentTree
     * @param wac
     * @return
     * @throws LegoException
     */
    private String singleInsert(CascadeTree parentTree, CascadeTree currentTree, ApplicationContext wac) throws LegoException {
        String uniqueValue = null;

        Map<String, Field> currentFields = currentTree.getFields();//当前字段的tree字段map
        String oiAlias = currentTree.getOi().getAlias();
        Connector connector = currentTree.getConnector();//连接器
        //新的map
        Map<String, Field> fields = new HashMap<>();
        fields.putAll(currentFields);

        if (parentTree != null) {
            //没有父级
            Map<String, Field> parentFields = parentTree.getFields();//父级tree的字段map
            if (connector != null) {
                //将连接器里的字段放到fields对象中
                String extraFieldName = connector.getFromFieldName();
                String toFieldName = connector.getToFieldName();
                Field _f = new Field();
                _f.setFieldName(extraFieldName);
                _f.setOiAlias(oiAlias);
                if (parentFields.containsKey(toFieldName)) {
                    _f.setFieldValue(parentFields.get(toFieldName).getFieldValue());
                } else {
                    _f.setFieldValue(parentTree.getUniqueValue());//用CascadeTree的uniqueValue 临时存储父表插入的唯一值
                }
                fields.put(extraFieldName, _f);
            }
        }

        //通过 oiAlias获取OI
        OI oi;
        try {
            oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        //通过oi.dsAlias获取DS
        DS ds;
        try {
            ds = tyDriver.getTyCacheService().getDSByAlias(oi.getDsAlias());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        //通过DST获取DataManager
        DBMT dbmt = DBMT.getDBMT(ds.getDst());
        if (dbmt == null) {
            throw new LegoException(ds.getDst() + "数据源不支持", ERROR_DB_DST_NOT_SUPPORT);
        }
        DataManager dataManager = (DataManager) wac.getBean(dbmt.getType());

        //转成OI的Field
        List<Field> createFieldsList = new ArrayList<Field>();
        try {
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);
            for (Field field : oiFields) {
                Field target = fields.get(field.getFieldName());
                if (target != null) {
                    field.setFieldValue(target.getFieldValue());
                    createFieldsList.add(field);
                }
            }
            uniqueValue = dataManager.create(oi, createFieldsList);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        return uniqueValue;
    }

    /**
     * TODO 针对自定义字段改造 fieldValue 不是下标的情况
     * 构造创建字段   以字段为单位
     * 结构：
     * index: [{
     * fieldName:xxx,
     * index:x,//下标值
     * connector
     * }]
     *
     * @param createFileds
     * @return
     */
    private Map<Integer, CascadeField> buildCreateFields(String srcOiAlias, List<InputField> createFileds) throws LegoException {
        //根据下标排序
        Collections.sort(createFileds, new Comparator<InputField>() {
            @Override
            public int compare(InputField o1, InputField o2) {
                //以前是用的fieldValue来表示下标值，这样会导致不能自定义其他参数，所以用alias来表示下标
//                    int i1 = Integer.valueOf(o1.fieldValueToString());
//                    int i2 = Integer.valueOf(o2.fieldValueToString());
                int i1 = 0;
                int i2 = 0;
                try {
                    i1 = Integer.valueOf(o1.getAlias());
                } catch (Exception e) {
                    return 1;
                }
                try {
                    i2 = Integer.valueOf(o2.getAlias());
                } catch (Exception e) {
                    return -1;
                }
                if (i1 > i2) {
                    return 1;
                }
                if (i1 == i2) {
                    return 0;
                }
                return -1;
            }
        });

        //下标作为key
        Map<Integer, CascadeField> map = new LinkedHashMap<>();//带排序的map
        for (InputField _if : createFileds) {
            String connectorPath = _if.getConnectorPath();
            String connectorAlais = null;//没有连接器的就是主表字段
            if (StringUtil.isValid(connectorPath)) {
                JSONArray connectorAliass = JSONArray.parseArray(connectorPath);
                connectorAlais = connectorAliass.getJSONObject(connectorAliass.size() - 1).getString("alias");
            }
            Connector connector = null;
            try {
                connector = tyDriver.getTyCacheService().getConnectorByAlias(connectorAlais);
            } catch (DBException e) {
                e.printStackTrace();
                throw new LegoException(e.getMessage(), e.getCode());
            }
            //常量下标,如果是自定义参数，下标为负数
            int index = -1;
            try {
                index = Integer.valueOf(_if.getAlias());
            } catch (Exception e) {
                //报错，说明alias不是下标，而是正常的字段别名。正常字段的下标都是负数
                while (map.containsKey(index)) {
                    index--;
                }
            }
            CascadeField cf = new CascadeField();
            cf.setFieldName(_if.getFieldName());
            cf.setFieldValue(_if.fieldValueToString());
            cf.setIndex(index);
            cf.setConnector(connector);
            cf.setOiAlias(connector == null ? srcOiAlias : connector.getFromOIAlias());
            map.put(index, cf);
        }
        return map;
    }

    /**
     * 通过下标数据生成每个表的字段信息  以表为单位
     *
     * @param map
     * @return
     */
    private Map<String, CascadeOi> buildCascadeOi(Map<Integer, CascadeField> map) {
        Map<String, CascadeOi> cos = new LinkedHashMap<>();//排序map
        for (Map.Entry<Integer, CascadeField> entry : map.entrySet()) {
            Integer index = entry.getKey();
            CascadeField cf = entry.getValue();
            String oiAlias = cf.getOiAlias();
            //map中有这张表
            if (cos.containsKey(oiAlias)) {
                cos.get(oiAlias).getIndexList().add(index);
            } else {
                //map中没有这张表
                CascadeOi co = new CascadeOi();
                co.setOiAlias(oiAlias);
                co.setConnector(cf.getConnector());
                List<Integer> indexList = new ArrayList<>();
                indexList.add(index);
                co.setIndexList(indexList);
                cos.put(oiAlias, co);
            }
        }
        return cos;
    }

    /**
     * 根据indexData数据生成 CascadeTree列表
     *
     * @param indexData
     * @param oiMap
     * @param fieldMap
     * @return
     */
    private List<CascadeTree> buildCascadeTree(List indexData, Map<String, CascadeOi> oiMap, Map<Integer, CascadeField> fieldMap) throws LegoException {
        if (CollectionUtil.isInvalid(indexData)) {
            throw new LegoException("缺少主数据", VendorExceptionCode.Lego_Error_Index_data_Null);
        }
        if (oiMap == null || fieldMap == null) {
            throw new LegoException("没有有效的导入字段配置", VendorExceptionCode.Lego_Error_CascadeAdd_Create_Field_Null);
        }

        //用于存储 上一次保存的CascadeTree对象，方便后面快速找到父级 <表名，CascadeTree>
        Map<String, CascadeTree> parentMap = new HashMap<>();

        //返回数据
        List<CascadeTree> resultList = new ArrayList<>();

        //遍历主数据    [[a1,a2,b1,b2]...]结构
        for (int i = 0; i < indexData.size(); i++) {
            Object datas = indexData.get(i);
            List<String> dataList = buildData(datas);

            //遍历oi map
            for (Map.Entry<String, CascadeOi> entry : oiMap.entrySet()) {
                CascadeOi coi = entry.getValue();
                List<Integer> indexList = coi.getIndexList();
                boolean noData = true;
                Map<String, Field> fields = new HashMap<>();
                Connector connector = null;
                String oiAlias = null;
                for (Integer index : indexList) {
                    //遍历某张表的下标
                    CascadeField cf = fieldMap.get(index);
                    Field f = new Field();
                    f.setOiAlias(cf.getOiAlias());
                    f.setFieldName(cf.getFieldName());
                    if (index >= 0) {
                        //有下标，数据从 主数据参数来获取
                        f.setFieldValue(dataList.get(index));
                        if (StringUtil.isValid(dataList.get(index))) {
                            //该表的所有字段都为空的时，说明 这行记录这张表是下一级的主表
                            noData = false;
                        }
                    } else {
                        //小于0 说明是自定义字段，没有下标，数据值从正常配置来
                        f.setFieldValue(cf.getFieldValue());
                    }
                    fields.put(cf.getFieldName(), f);
                    connector = cf.getConnector();
                    oiAlias = cf.getOiAlias();
                }
                if (noData) {
                    //都没有数据， 说明继续往下一层查找
                    continue;
                }
                OI oi = new OI();
                oi.setAlias(oiAlias);

                //生成该表的CascadeTree
                CascadeTree ct = new CascadeTree();
                ct.setConnector(connector);
                ct.setChildren(new ArrayList<>());
                ct.setFields(fields);
                ct.setOi(oi);
                //放到临时变量中
                parentMap.put(oiAlias, ct);

                if (connector == null) {
                    //说明是主表  添加到返回列表中
                    resultList.add(ct);
                } else {
                    String toOIAlias = connector.getToOIAlias();//上一级oi别名
                    CascadeTree parentCt = parentMap.get(toOIAlias);//获取父级CascadeTree
                    parentCt.getChildren().add(ct);
                }
            }
        }
        return resultList;
    }

    /**
     * 兼容多种格式数据
     *
     * @param datas
     * @return
     */
    private List<String> buildData(Object datas) {
        if (datas instanceof List) {
            return (List<String>) datas;
        } else if (datas instanceof String[]) {
            return Arrays.asList((String[]) datas);
        } else if (datas instanceof JSONArray) {
            return JSONArray.parseArray(JSONArray.toJSONString(datas), String.class);
        }
        return null;
    }


    /**
     * 级联字段
     */
    class CascadeField {
        private String fieldName;//字段名
        private String fieldValue;//字段值
        private int index;//字段下标
        private String oiAlias;//字段所属oi
        private Connector connector;//连接器对象,为空表示主表

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getOiAlias() {
            return oiAlias;
        }

        public void setOiAlias(String oiAlias) {
            this.oiAlias = oiAlias;
        }

        public Connector getConnector() {
            return connector;
        }

        public void setConnector(Connector connector) {
            this.connector = connector;
        }
    }

    /**
     * 级联oi
     */
    class CascadeOi {
        private String oiAlias;//是哪张表
        private List<Integer> indexList;//这张表中字段在哪几列
        private Connector connector;//连接器

        public String getOiAlias() {
            return oiAlias;
        }

        public void setOiAlias(String oiAlias) {
            this.oiAlias = oiAlias;
        }

        public List<Integer> getIndexList() {
            return indexList;
        }

        public void setIndexList(List<Integer> indexList) {
            this.indexList = indexList;
        }

        public Connector getConnector() {
            return connector;
        }

        public void setConnector(Connector connector) {
            this.connector = connector;
        }
    }

    /**
     * 级联树  代替connectorTree
     */
    class CascadeTree {
        private Connector connector;
        private String uniqueValue;
        private OI oi;
        private Map<String, Field> fields;
        private List<CascadeTree> children;

        public Connector getConnector() {
            return connector;
        }

        public void setConnector(Connector connector) {
            this.connector = connector;
        }

        public String getUniqueValue() {
            return uniqueValue;
        }

        public void setUniqueValue(String uniqueValue) {
            this.uniqueValue = uniqueValue;
        }

        public OI getOi() {
            return oi;
        }

        public void setOi(OI oi) {
            this.oi = oi;
        }

        public Map<String, Field> getFields() {
            return fields;
        }

        public void setFields(Map<String, Field> fields) {
            this.fields = fields;
        }

        public List<CascadeTree> getChildren() {
            return children;
        }

        public void setChildren(List<CascadeTree> children) {
            this.children = children;
        }
    }

}
