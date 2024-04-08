package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.base.bean.constant.CT;
import com.greatbee.db.bean.constant.DBMT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.db.bean.oi.Connector;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.base.bean.view.Condition;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.bean.view.MultiCondition;
import com.greatbee.db.database.RelationalDataManager;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 列表 套列表
 * <p/>
 * 输入：
 * 1. 需要读取的字段列表(多个ioft= read)
 * 2. 需要读取的条件(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * 6. Input_Key_Keywords 全局搜索关键字
 * 7. Input_Key_Global_Search_Fields 全局搜索字段列表，只支持当前表的字段全局搜索
 *
// * 8.childOI:子信息的OI
 * 9.connectorAlias：父子oi的连接器     0329新增 支持多个级联
 * 输出：
 * 1. 列表数据 Output_Key_Tree_List
 * 2. 输出各个字段的列表(IOFT=read)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("listTree")
public class ListTree extends BaseRead implements Lego, ExceptionCode {

    private static Logger logger = Logger.getLogger(ListTree.class);

    private static final String Output_Key_Tree_List = "tree_list";
//    private static final String Input_Key_Child_OI="childOI";
    private static final String Input_Key_Connector_Alias="connectorAlias";

    private static final String Input_Key_Childs_Keys = "childKeys";//和上面connectorAlias 一一对应，表示子列表的key

    @Autowired
    protected TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        String rootOIAlias = input.getApiLego().getOiAlias();
        ConnectorTree root = buildConnectorTree(tyDriver, input);

        //全局搜索关键字 处理全局搜索
        buildKeywords(input,root);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            DataList dataList = dataManager.list(root);

            //读取Read的字段，写入到Output中
            if (dataList != null && CollectionUtil.isValid(dataList.getList())) {
                buildOutputFields(output, dataList.getList());

                //添加子列表
                InputField connectorField = input.getInputField(Input_Key_Connector_Alias);
                String childKeys = input.getInputValue(Input_Key_Childs_Keys);
                if(connectorField!=null&&connectorField.getFieldValue()!=null){
                    String[] connectors = connectorField.fieldValueToString().split(",");
                    String[] childKeyArray = null;
                    if(StringUtil.isValid(childKeys)){
                        childKeyArray = childKeys.split(",");
                    }
                    for(int k=0;k<connectors.length;k++){
                        //遍历所有的连接器
                        String connector = connectors[k];
                        Connector conn = tyDriver.getTyCacheService().getConnectorByAlias(connector);
                        if(conn==null||!conn.getToOIAlias().equalsIgnoreCase(rootOIAlias)){
                            throw new LegoException("连接器配置错误",300001);
                        }
                        OI fromOI = tyDriver.getTyCacheService().getOIByAlias(conn.getFromOIAlias());
                        java.util.List<Field> fields = tyDriver.getTyCacheService().getFields(conn.getFromOIAlias());

                        for(int i=0;i<dataList.getList().size();i++){
                            Data data = (Data) dataList.getList().get(i);
                            String aliaskey = this.getAliasKey(conn.getToFieldName(),input);
                            if(StringUtil.isInvalid(aliaskey)){
                                logger.info("[ListTree] aliaskey is invalid");
                                continue;
                            }
                            //组装子列表条件
                            java.util.List<Condition> conList = new ArrayList<Condition>();
                            Condition c = new Condition();
                            c.setConditionFieldName(conn.getFromFieldName());
                            c.setConditionFieldValue(data.getString(aliaskey));
                            c.setCt(CT.EQ.getName());
                            conList.add(c);
                            MultiCondition condition = new MultiCondition(conList);

                            DataList chidList = dataManager.list(fromOI, fields, condition);
                            String _key = (childKeyArray!=null&&StringUtil.isValid(childKeyArray[k]))?childKeyArray[k]:conn.getAlias();
                            data.put(_key,chidList);
                        }
                    }
                }
            }
            output.setOutputValue(Output_Key_Tree_List, dataList);

        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }

    /**
     * 获取字段名对应的别名
     * @param fieldName
     * @param input
     * @return
     */
    private String getAliasKey(String fieldName,Input input){
        //查询父信息所有的字段
        java.util.List<InputField> ifs = input.getInputField(IOFT.Read);
        for(int i=0;i<ifs.size();i++){
            if(ifs.get(i).getFieldName().equalsIgnoreCase(fieldName)){
                return ifs.get(i).getAlias();
            }
        }
//        Map<String, Field> map = new HashMap<String, Field>();
//        BuildUtils.buildAllFields(root, map);
//        for(Map.Entry<String,Field> entry:map.entrySet()){
//            Field f = entry.getValue();
//            if(f.getFieldName().equalsIgnoreCase(fieldName)){
//                return entry.getKey();
//            }
//        }
        return "";
    }
}
