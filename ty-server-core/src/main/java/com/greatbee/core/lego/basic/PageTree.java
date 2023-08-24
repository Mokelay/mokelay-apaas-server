package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.CT;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.oi.Connector;
import com.greatbee.core.bean.oi.Field;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.view.Condition;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.bean.view.MultiCondition;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.util.SpringContextUtil;
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
 * 6. 请求页码 Key_Page
 * 7. 每页的页数 Key_PageSize
 * 9.connectorAlias：父子oi的连接器
 * 输出：
 * 1. 翻页数据 Output_Key_Tree_Page
 * 2. 输出各个字段的列表(IOFT=read)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("pageTree")
public class PageTree extends BaseRead implements Lego, ExceptionCode {

    private static Logger logger = Logger.getLogger(PageTree.class);

    private static final String Input_Key_Page = "page";
    private static final String Input_Key_PageSize = "pageSize";

    private static final String Output_Key_Tree_Page = "tree_page";
    private static final String Input_Key_Connector_Alias="connectorAlias";

    @Autowired
    protected TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        int page = DataUtil.getInt(input.getInputValue(Input_Key_Page), 1);
        int pageSize = DataUtil.getInt(input.getInputValue(Input_Key_PageSize), 10);

        if(page<=0){
            page=1;
        }
        if(pageSize<=0){
            pageSize=1;
        }

        String rootOIAlias = input.getApiLego().getOiAlias();
        ConnectorTree root = buildConnectorTree(tyDriver, input);

        //全局搜索关键字 处理全局搜索
        buildKeywords(input,root);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {

            DataPage dataPage = dataManager.page(page, pageSize, root);

            //读取Read的字段，写入到Output中
            if (dataPage != null && CollectionUtil.isValid(dataPage.getCurrentRecords())) {
                buildOutputFields(output, dataPage.getCurrentRecords());

                //添加子列表
                InputField connectorField = input.getInputField(Input_Key_Connector_Alias);
                if(connectorField!=null){
                    Connector conn = tyDriver.getTyCacheService().getConnectorByAlias((String) connectorField.getFieldValue());
                    if(conn==null||!conn.getToOIAlias().equalsIgnoreCase(rootOIAlias)){
                        throw new LegoException("连接器配置错误",300001);
                    }
                    OI fromOI = tyDriver.getTyCacheService().getOIByAlias(conn.getFromOIAlias());
                    java.util.List<Field> fields = tyDriver.getTyCacheService().getFields(conn.getFromOIAlias());

                    for(int i=0;i<dataPage.getCurrentRecords().size();i++){
                        Data data = (Data) dataPage.getCurrentRecords().get(i);
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
                        data.put(conn.getAlias(),chidList);
                    }
                }
            }
            output.setOutputValue(Output_Key_Tree_Page, dataPage);

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
        return "";
    }
}
