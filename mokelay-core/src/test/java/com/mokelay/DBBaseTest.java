package com.mokelay;

import com.alibaba.fastjson.JSONObject;

// import org.junit.Test;


/**
 * Author: CarlChen
 * Date: 2017/11/21
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:test_server.xml")
public class DBBaseTest extends MokelayBaseTest {
//    protected ApplicationContext context;

    /**
     * Test Context
     */
    public void testContext() {
        System.out.println(context);
    }


    /**
     * 输出json数据
     *
     * @param o
     */
    protected void printJSONObject(Object o) {
        System.out.println(JSONObject.toJSONString(o));
    }
}
