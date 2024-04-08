package com.greatbee.db.database.mysql.testcase;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.view.DSView;
import com.greatbee.db.database.mysql.MysqlRelationalDataManagerTest;
import org.junit.Test;

/**
 * Created by usagizhang on 18/3/14.
 */
public class TestExportFromPhysicsDS extends MysqlRelationalDataManagerTest implements ExceptionCode {
    public TestExportFromPhysicsDS() {
        this.setUp();
    }

    @Test
    public void testMain() {
        try {
            DSView dsv = this.mysqlDataManager.exportFromPhysicsDS(this.getDS());
            System.out.println(JSONObject.toJSONString(dsv).toString());
        } catch (DBException e) {
            e.printStackTrace();
        }
    }


}
