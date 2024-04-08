package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.base.bean.constant.CT;
import com.greatbee.db.bean.constant.DBMT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.database.RelationalDataManager;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.manager.utils.CustomBuildUtils;
import com.greatbee.api.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 列表
 * <p/>
 * 输入：
 * 1. 需要读取的字段列表(多个ioft= read)
 * 2. 需要读取的条件(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * 6. Input_Key_Keywords 全局搜索关键字
 * 7. Input_Key_Global_Search_Fields 全局搜索字段列表，只支持当前表的字段全局搜索
 * 8. Input_Key_Main_Key 主键key  默认id
 * 9. Input_Key_Parent_Key 父级key 默认parentId
 * 输出：
 * 1. 列表数据 Output_Key_Data_List
 * 2. 输出各个字段的列表(IOFT=read)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("treeGridList")
public class TreeGridList extends BaseRead implements Lego, ExceptionCode {
    private static final String Output_Key_Data_List = "data_list";
    //树形主键key
    private static final String Input_Key_Main_Key = "main_key";
    //树形父key
    private static final String Input_Key_Parent_Key = "parent_key";

    //子数量字段名
    private static final String CHILD_NUM = "child_num";


    @Autowired
    protected TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        ConnectorTree root = buildConnectorTree(tyDriver, input);

        //全局搜索关键字 处理全局搜索
        buildKeywords(input,root);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            DataList dataList = dataManager.list(root);

            InputField mainKeyField = input.getInputField(Input_Key_Main_Key);
            InputField parentKeyField = input.getInputField(Input_Key_Parent_Key);

            String mainKey = DataUtil.getString(mainKeyField.getFieldValue(), "id");
            String parentKey = DataUtil.getString(parentKeyField.getFieldValue(),"parentId");
            mainKey = StringUtil.isValid(mainKey)?mainKey:"id";
            parentKey = StringUtil.isValid(parentKey)?parentKey:"parentId";

            if(dataList!=null&&dataList.getList()!=null&&dataList.getList().size()>0){
                java.util.List<Data> list = dataList.getList();
                for(Data data:list){
                    int count =0;
                    if(StringUtil.isInvalid(data.getString(mainKey))){
                        data.put(CHILD_NUM,0);
                        continue;
                    }
//                    for(Data tmpData:list){
//                        if(data.getString(mainKey).equals(tmpData.getString(parentKey))) {
//                            count++;
//                        }
//                    }
                    java.util.List<InputField> con = new ArrayList<>();
                    InputField con1 = new InputField();
                    con1.setCt(CT.EQ.getName());
                    con1.setFieldName(parentKey);
                    con1.setIft(IOFT.Condition.getType());
                    con1.setFieldValue(data.getString(mainKey));
                    con.add(con1);
                    ConnectorTree countTree = CustomBuildUtils.customBuildSelectConnectorTree(tyDriver, input.getApiLego().getOiAlias(), null, con);
                    count = dataManager.count(countTree);

                    data.put(CHILD_NUM,count);
                }
            }

            output.setOutputValue(Output_Key_Data_List, dataList);

            //读取Read的字段，写入到Output中
            if (dataList != null && CollectionUtil.isValid(dataList.getList())) {
                buildOutputFields(output, dataList.getList());
            }
        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }
}
