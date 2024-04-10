package com.mokelay.db.database.base;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.bean.constant.DBTT;

import java.sql.*;

/**
 * Created by usagizhang on 18/3/13.
 */
public abstract class BaseTransactionTemplate extends BaseTYJDBCTemplate {

    /**
     * 事务类型
     */
    private DBTT dbtt;

    protected PreparedStatement ps = null;

    protected ResultSet rs = null;

    /**
     * 执行事务主函数
     *
     * @param conn
     * @throws Exception
     */
    public void execute(Connection conn) throws SQLException, DBException {
        /**
         * 校验参数内容
         */
        //检查链接是否是正常
        if (conn == null) {
            //数据库连接已关闭
        }

        /**
         * 执行钩子函数
         */
        this.executeTransaction(conn);

        //释放ps,rs对象
        this.release();
    }

    /**
     * 释放资源
     *
     * @throws DBException
     */
    private void release() throws DBException, SQLException {
        this.releaseResultSet(rs);
        this.releasePreparedStatement(ps);
    }

    /**
     * 执行事务的钩子函数
     *
     * @throws Exception
     */
    protected abstract void executeTransaction(Connection conn) throws SQLException;


    public DBTT getDbtt() {
        return dbtt;
    }

    public void setDbtt(DBTT dbtt) {
        this.dbtt = dbtt;
    }

}

