package com.mokelay.db.database.mysql.testcase;

import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.DBException;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.database.mysql.MysqlRelationalDataManagerTest;
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
