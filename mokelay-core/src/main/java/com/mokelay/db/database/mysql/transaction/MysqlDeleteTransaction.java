package com.mokelay.db.database.mysql.transaction;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.bean.constant.DBTT;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.database.base.BaseTransactionTemplate;
import com.mokelay.base.bean.view.Condition;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by usagizhang on 18/3/13.
 */
public class MysqlDeleteTransaction extends BaseTransactionTemplate {
    private static Logger logger = Logger.getLogger(MysqlDeleteTransaction.class);

    private Field pkField;
    private Condition condition;
    private StringBuilder sql;
    private boolean isDeleteByPK = false;

    public MysqlDeleteTransaction(OI oi, Field pkField) throws DBException {
        this.setDbtt(DBTT.Delete);
        this.pkField = pkField;

        sql = new StringBuilder("DELETE FROM ");
        sql.append("`").append(oi.getResource()).append("` ");
        // SQL Value用?处理
        sql.append(" WHERE `").append(pkField.getFieldName()).append("`=? ");
        logger.info("组装删除对象SQL：" + sql.toString());
        isDeleteByPK = true;
    }

    public MysqlDeleteTransaction(OI oi, Condition condition) throws DBException {
        this.setDbtt(DBTT.Delete);
        this.condition = condition;
        sql = new StringBuilder("DELETE FROM ");
        sql.append("`").append(oi.getResource()).append("` ");
        if (condition != null) {
            sql.append(" WHERE ");
            Condition.buildConditionSql(sql, condition);
        }

        logger.info("组装删除对象SQL：" + sql.toString());
    }

    /**
     * 执行事务
     *
     * @param conn
     * @throws Exception
     */
    @Override
    public void executeTransaction(Connection conn) throws SQLException {
        logger.info("执行删除对象SQL：" + sql.toString());
        /**
         * 创建ps对象
         */
        ps = conn.prepareStatement(sql.toString());
        /**
         * 设置参数
         */
        if (isDeleteByPK) {
            _setPsParamPk(1, ps, pkField);
        } else {
            int index = Condition.buildConditionSqlPs(1, ps, condition);//前面没有？参数，所以从1开始,条件后面也可以再添加参数，索引从index开始
        }
        /**
         * 执行更新
         */
        ps.executeUpdate();
    }
}
