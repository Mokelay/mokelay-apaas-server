package com.greatbee.db.database.mysql.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.database.mysql.MysqlSchemaDataManagerTest;

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