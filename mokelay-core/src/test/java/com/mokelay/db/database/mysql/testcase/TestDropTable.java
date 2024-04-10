package com.mokelay.db.database.mysql.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.database.mysql.MysqlSchemaDataManagerTest;

import org.junit.Test;

public class TestDropTable extends MysqlSchemaDataManagerTest implements ExceptionCode {
    public TestDropTable() {
        this.setUp();
    }

    @Test
    public void testDropTableByDefault() throws DBException {
        this.mysqlDataManager.dropTable(this.initOI("测试表", "ly_base_app", "ly_base_app"));
    }

}