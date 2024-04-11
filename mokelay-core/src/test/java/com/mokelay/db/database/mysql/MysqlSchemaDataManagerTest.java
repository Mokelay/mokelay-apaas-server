
package com.mokelay.db.database.mysql;

import com.mokelay.db.ExceptionCode;
import com.mokelay.db.database.SchemaDataManager;

/**
 * 测试 Mysql Schema Data Manager
 */
public abstract class MysqlSchemaDataManagerTest extends MysqlDataManagerTest implements ExceptionCode {

    public SchemaDataManager mysqlDataManager;

    /**
     * setUp 设置测试用例
     */
    public void setUp() {
        super.setUp();
        //加载manager
        mysqlDataManager = (SchemaDataManager) context.getBean("mysqlDataManager");
    }
}