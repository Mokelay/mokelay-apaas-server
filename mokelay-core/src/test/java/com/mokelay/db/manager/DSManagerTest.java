package com.mokelay.db.manager;

import com.mokelay.MokelayBaseTest;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.constant.DST;

/**
 * Created by CarlChen on 2017/5/24.
 */
public class DSManagerTest extends MokelayBaseTest {
    private DSManager dsManager;

    public void setUp() {
        super.setUp();

        dsManager = (DSManager) context.getBean("dsManager");
    }


    /**
     * add DS
     *
     * @throws Exception
     */
    public void testAddDS() throws Exception {
        DS ds = new DS();
        ds.setName("数据源");
        ds.setDescription("测试数据源");
        ds.setAlias("test_alias");
        ds.setDst(DST.Mysql.getType());
        ds.setConnectionUrl("mysql");
        ds.setConnectionUsername("root");
        ds.setConnectionPassword("qazWSXedc");
        dsManager.add(ds);
    }
}
