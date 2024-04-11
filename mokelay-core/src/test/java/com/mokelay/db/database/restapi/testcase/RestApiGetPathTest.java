package com.mokelay.db.database.restapi.testcase;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.constant.DT;
import com.mokelay.db.bean.constant.RestApiFieldGroupType;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.bean.view.RestApiResponse;
import com.mokelay.db.database.restapi.basecase.BaseConnectTest;

import java.util.ArrayList;
import java.util.List;

/**
 * test post
 * Created by usagizhang on 18/3/28.
 */
public class RestApiGetPathTest extends BaseConnectTest {
    public RestApiGetPathTest() throws DBException {
        /**
         * URMS登录
         */
        this.URMSLogin();
    }

    @Override
    protected void runTest() throws DBException {
        printJSONObject("login token -> " + this.getAuthToken());
        OI oi = this.initOI("test_rest_ty", "/config/{alias}", "test_get");
        List<Field> fields = new ArrayList<Field>();
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Path, "alias", "test-rest-api-get-demo"));
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Get, "username", "王子恒"));
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Header, "x-auth-token", this.getAuthToken()));
        RestApiResponse restApiResponse = (RestApiResponse) restAPIManager.connect(null,oi, fields);
        printJSONObject(restApiResponse);
    }
}
