package com.greatbee.core.db.base;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.oi.DS;
import com.greatbee.core.bean.oi.Field;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.view.Condition;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.bean.view.DSView;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.handler.DataHandler;
import com.greatbee.core.handler.QueryHandler;
import com.greatbee.core.util.BuildUtils;
import com.greatbee.core.util.DataSourceUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * SQL Server Data Manager
 * <p>
 * Author: xuechao.zhang
 * Date: 2017/11/18
 */
public abstract class DataManager extends DBManager implements RelationalDataManager {

    private static Logger logger = Logger.getLogger(DataManager.class);

    @Override
    public Data read(ConnectorTree connectorTree) throws DBException {
        DataList dl = this.list(connectorTree);
        List datas = dl.getList();
        return CollectionUtil.isValid(datas) ? (Data) datas.get(0) : new Data();
    }

    @Override
    public DataPage page(int page, int pageSize, ConnectorTree connectorTree) throws DBException {
        if (connectorTree != null && connectorTree.getOi() != null) {
            OI oi = connectorTree.getOi();
            int count = this.count(connectorTree);
            HashMap map = new HashMap();
            BuildUtils.buildAllFields(connectorTree, map);
            return this.executePageQuery(oi.getDsAlias(), page, pageSize, count, new QueryHandler() {
                @Override
                public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                    return buildingPageQuery(page, pageSize, connectorTree, conn, ps);
                }
            }, new DataHandler() {
                @Override
                public void execute(ResultSet rs, Data data) throws SQLException {
                    Data item = new Data();
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        buildingDataObject(rs, entry, data);
                    }
                }
            });
        } else {
            throw new DBException("查询条件无效", ERROR_DB_CONT_IS_NULL);
        }
    }

    @Override
    public DataList list(ConnectorTree connectorTree) throws DBException {
        if (connectorTree != null && connectorTree.getOi() != null) {
            OI oi = connectorTree.getOi();
            HashMap map = new LinkedHashMap<>();
            BuildUtils.buildAllFields(connectorTree, map);

            return this.executeListQuery(oi.getDsAlias(), new QueryHandler() {
                @Override
                public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                    return buildingListQuery(connectorTree, conn, ps);
                }
            }, new DataHandler() {
                @Override
                public void execute(ResultSet rs, Data data) throws SQLException {
                    Iterator iterator = map.entrySet().iterator();
//                    Data data = new Data();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        //logger.info("entry=" + JSONObject.toJSONString(entry));
                        buildingDataObject(rs, entry, data);
                    }
//                    return data;
                }
            });

        } else {
            throw new DBException("查询条件无效", ERROR_DB_CONT_IS_NULL);
        }
    }

    @Override
    public int count(ConnectorTree connectorTree) throws DBException {
        int result = 0;
        if (connectorTree != null && connectorTree.getOi() != null) {
            OI oi = connectorTree.getOi();
            return executCountQuery(oi.getDsAlias(), new QueryHandler() {
                @Override
                public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                    return buildingCountQuery(connectorTree, conn, ps);
                }
            });
        } else {
            throw new DBException("查询条件无效", ERROR_DB_CONT_IS_NULL);
        }
    }

    public int count(OI oi, Condition condition) throws DBException {
        return executCountQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                return buildingCountQuery(oi, condition, conn, ps);
            }
        });
    }

    @Override
    public Data read(OI oi, List<Field> fields, Field pkField) throws DBException {
        return this.executeReadQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                return buildingReadQuery(oi, fields, pkField, conn, ps);
            }
        }, new DataHandler() {
            @Override
            public void execute(ResultSet rs, Data data) throws SQLException {
                buildingDataObject(rs, fields, data);
            }
        });
    }

    @Override
    public DataPage page(OI oi, List<Field> fields, int page, int pageSize, Condition condition) throws DBException {
        int count = this.count(oi, condition);
        return this.executePageQuery(oi.getDsAlias(), page, pageSize, count, new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                return buildingPageQuery(oi, fields, page, pageSize, condition, conn, ps);
            }
        }, new DataHandler() {
            @Override
            public void execute(ResultSet rs, Data data) throws SQLException {
                buildingDataObject(rs, fields, data);
            }
        });
    }

    @Override
    public DataList list(OI oi, List<Field> fields, Condition condition) throws DBException {

        return this.executeListQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException {
                return buildingListQuery(oi, fields, condition, conn, ps);
            }
        }, new DataHandler() {
            @Override
            public void execute(ResultSet rs, Data data) throws SQLException {
                buildingDataObject(rs, fields, data);
            }
        });
    }

    @Override
    public void delete(OI oi, Field pkField) throws DBException {
        this.executUpdateQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException, DBException {
                return buildingDeleteQuery(oi, pkField, conn, ps);
            }
        });
    }

    @Override
    public void delete(OI oi, Condition condition) throws DBException {
        this.executUpdateQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws SQLException, DBException {
                return buildingDeleteQuery(oi, condition, conn, ps);
            }
        });
    }

    @Override
    public String create(OI oi, List<Field> fields) throws DBException {
        DataSource _ds = DataSourceUtils.getDatasource(oi.getDsAlias(), this.dsManager);
        if (_ds == null) {
            throw new DBException("获取数据源失败", ERROR_DB_DS_NOT_FOUND);
        } else {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            String result;
            try {
                conn = _ds.getConnection();
                /**
                 * 禁止自动提交
                 */
                conn.setAutoCommit(false);
                result = this.executeCreateQuery(oi, fields, conn, ps);
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                    throw new DBException("数据库事务执行失败,回滚成功."+e.getMessage(), ERROR_DB_TRANSACTION_ERROR);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    throw new DBException("数据库事务执行失败,回滚失败."+e.getMessage(), ERROR_DB_TRANSACTION_ERROR);
                }
            } finally {
                this.releaseResultSet(rs);
                this.releasePreparedStatement(ps);
                this.releaseConnection(conn);
            }

            return result;
        }
    }

    @Override
    public void update(OI oi, List<Field> fields, Field pkField) throws DBException {
        this.executUpdateQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws DBException, SQLException {
                return buildingUpdateQuery(oi, fields, pkField, conn, ps);
            }
        });
    }

    @Override
    public void update(OI oi, List<Field> fields, Condition condition) throws DBException {
        this.executUpdateQuery(oi.getDsAlias(), new QueryHandler() {
            @Override
            public PreparedStatement execute(Connection conn, PreparedStatement ps) throws DBException, SQLException {
                return buildingUpdateQuery(oi, fields, condition, conn, ps);
            }
        });
    }

    @Override
    public void executeTransaction(DS ds, List<BaseTransactionTemplate> transactionNodes) throws DBException {
        if (CollectionUtil.isInvalid(transactionNodes)) {
            //没有需要执行的事务组件
            throw new DBException("没有需要执行的事务组件", ERROR_DB_DS_NOT_FOUND);
        }
        Connection conn = null;
        try {
            conn = this.getConnection(ds.getAlias());
            /**
             * 禁止自动提交
             */
            conn.setAutoCommit(false);
            for (BaseTransactionTemplate node : transactionNodes) {
                /**
                 * 执行事务节点
                 */
                node.execute(conn);
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                /**
                 * 回滚内容
                 */
                conn.rollback();
                throw new DBException("数据库事务执行失败,回滚成功", ERROR_DB_TRANSACTION_ERROR);
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new DBException("数据库事务执行失败,回滚失败", ERROR_DB_TRANSACTION_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException("数据库事务执行失败,回滚失败", ERROR_DB_TRANSACTION_ERROR);
        } finally {
            this.releaseConnection(conn);
        }
    }

    @Override
    public abstract DSView exportFromPhysicsDS(DS ds) throws DBException;

    @Override
    public abstract void importFromDS(DSView dsView) throws DBException;

    public abstract PreparedStatement buildingPageQuery(int page, int pageSize, ConnectorTree connectorTree, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract void buildingDataObject(ResultSet rs, Map.Entry entry, Data data) throws SQLException;

    public abstract PreparedStatement buildingListQuery(ConnectorTree connectorTree, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract PreparedStatement buildingCountQuery(OI oi, Condition condition, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract PreparedStatement buildingCountQuery(ConnectorTree connectorTree, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract PreparedStatement buildingReadQuery(OI oi, List<Field> fields, Field pkField, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract PreparedStatement buildingListQuery(OI oi, List<Field> fields, Condition condition, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract PreparedStatement buildingPageQuery(OI oi, List<Field> fields, int page, int pageSize, Condition condition, Connection conn, PreparedStatement ps) throws SQLException;

    public abstract void buildingDataObject(ResultSet rs, List<Field> fields, Data data) throws SQLException;

    public abstract PreparedStatement buildingDeleteQuery(OI oi, Condition condition, Connection conn, PreparedStatement ps) throws SQLException, DBException;

    public abstract String executeCreateQuery(OI oi, List<Field> fields, Connection conn, PreparedStatement ps) throws SQLException, DBException;

    public abstract PreparedStatement buildingDeleteQuery(OI oi, Field pkField, Connection conn, PreparedStatement ps) throws SQLException, DBException;

    public abstract PreparedStatement buildingUpdateQuery(OI oi, List<Field> fields, Field pkField, Connection conn, PreparedStatement ps) throws DBException, SQLException;

    public abstract PreparedStatement buildingUpdateQuery(OI oi, List<Field> fields, Condition condition, Connection conn, PreparedStatement ps) throws DBException, SQLException;
}
