package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.api.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 条件读取数据的总记录条数
 * <p/>
 * 输入：
 * 1. 需要读取的字段列表(多个ioft= read)
 * 2. 需要读取的条件(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * 输出：
 * 1. 翻页数据 OUTPUT_KEY_COUNT
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("count")
public class Count extends BaseRead implements Lego, ExceptionCode {

    private static final String OUTPUT_KEY_COUNT = "count";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        ConnectorTree root = buildConnectorTree(tyDriver, input);

        //全局搜索关键字 处理全局搜索
        buildKeywords(input,root);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            int count = dataManager.count(root);
            output.setOutputValue(OUTPUT_KEY_COUNT, count);
        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }
}
