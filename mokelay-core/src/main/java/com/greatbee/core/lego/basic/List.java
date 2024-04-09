package com.greatbee.core.lego.basic;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.constant.DBMT;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.database.RelationalDataManager;
import com.greatbee.core.lego.*;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * 输出：
 * 1. 列表数据 Output_Key_Data_List
 * 2. 输出各个字段的列表(IOFT=read)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("list")
public class List extends BaseRead implements Lego,LegoGenerator, ExceptionCode {

    private static final Logger logger = Logger.getLogger(List.class);

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
            //加上序号字段
            LegoUtil.buildListIndex(dataList);
            output.setOutputValue(Output_Key_Data_List, dataList);

            //读取Read的字段，写入到Output中
            if (dataList != null && CollectionUtil.isValid(dataList.getList())) {
                buildOutputFields(output, dataList.getList());
            }
        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }

    @Override
    public void generate(int apiLegoId, String oiAlias) throws LegoException {
        /*
        2024-04-09 废弃数据库存储
        TODO 后续升级为JSON存储 ,见ty_model_to_api_and_page的实现方式
        try {
            java.util.List<Field> fields = tyDriver.getTyCacheService().getFields(oiAlias);
            //添加inputfields
            this.buildListAndPageGenerate(tyDriver,fields,apiLegoId,true);

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_GENERATE);
        }
         */

    }


}
