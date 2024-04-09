package com.greatbee.core.lego.basic;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
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
 * 翻页读取数据
 * <p/>
 * 输入：
 * 1. 需要读取的字段列表(多个ioft= read)
 * 2. 需要读取的条件(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * 6. 请求页码 Key_Page
 * 7. 每页的页数 Key_PageSize
 * 输出：
 * 1. 翻页数据 OUTPUT_KEY_PAGE_DATA
 * 2. 输出各个字段的列表(IOFT=read)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("page")
public class Page extends BaseRead implements Lego,LegoGenerator, ExceptionCode {

    private static final Logger logger = Logger.getLogger(Page.class);

    @Autowired
    private TYDriver tyDriver;

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

        ConnectorTree root = buildConnectorTree(tyDriver, input);

        //全局搜索关键字 处理全局搜索
        buildKeywords(input,root);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            DataPage dataPage = dataManager.page(page, pageSize, root);
            //加上序号字段
            LegoUtil.buildListIndex(dataPage);
            output.setOutputValue(OUTPUT_KEY_PAGE_DATA, dataPage);

            //读取Read的字段，写入到Output中
            if (dataPage != null && CollectionUtil.isValid(dataPage.getCurrentRecords())) {
                buildOutputFields(output, dataPage.getCurrentRecords());
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
            this.buildListAndPageGenerate(tyDriver,fields,apiLegoId,false);

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_GENERATE);
        }
         */
    }
}
