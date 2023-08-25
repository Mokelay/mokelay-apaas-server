package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 根据输入的Condition，需要判断是否唯一，即取的data为空，如果不为空，则抛出异常
 * <p/>
 * 输入：
 * 1. 需要检查的条件Condition(IOFT = condition)，采用Select count(*)
 * 输出：
 * 2. 是否唯一 Output_Key_Is_Unique   ture表示可以后面任务，false表示已经存在
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("uniqueness")
public class Uniqueness extends BaseRead implements Lego {
    private static final String Output_Key_Is_Unique = "is_unique";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        //不组装read字段
        ConnectorTree tree = buildConnectorTree(tyDriver, input,true);
        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            output.setOutputValue(Output_Key_Is_Unique, !(dataManager.count(tree) > 0));
        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

    }
}
