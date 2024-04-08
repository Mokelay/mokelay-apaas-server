package com.greatbee.db.database.restapi.testcase;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.constant.DT;
import com.greatbee.db.bean.constant.RestApiFieldGroupType;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.db.bean.view.RestApiResponse;
import com.greatbee.db.database.restapi.basecase.BaseConnectTest;

import java.util.ArrayList;
import java.util.List;

/**
 * test post
 * Created by usagizhang on 18/3/28.
 */
public class RestApiPostTest extends BaseConnectTest {
    public RestApiPostTest() throws DBException {
        /**
         * URMS登录
         */
        this.URMSLogin();
    }

    @Override
    protected void runTest() throws DBException {
        printJSONObject("login token -> " + this.getAuthToken());
        OI oi = this.initOI("test_rest_ty", "/config/test-rest-api-post-demo", "test_post");
        List<Field> fields = new ArrayList<Field>();
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Post, "sex", "1"));
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Header, "x-auth-token", this.getAuthToken()));
        fields.add(initField(DT.String, 128, RestApiFieldGroupType.Method, "post", "post"));
        RestApiResponse restApiResponse = (RestApiResponse) restAPIManager.connect(null,oi, fields);
        printJSONObject(restApiResponse);
    }
}