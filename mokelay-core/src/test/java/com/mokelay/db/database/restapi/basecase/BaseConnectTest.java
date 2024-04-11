package com.mokelay.db.database.restapi.basecase;

import com.mokelay.base.bean.DBException;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.database.restapi.RestAPIManagerTest;
import org.junit.Test;

/**
 * Created by usagizhang on 18/3/28.
 */
public abstract class BaseConnectTest extends RestAPIManagerTest implements ExceptionCode {

    public BaseConnectTest() throws DBException {
        this.setUp();
    }

    @Test
    public void startTest() throws DBException {
        this.runTest();
    }

    protected abstract void runTest() throws DBException;
}
