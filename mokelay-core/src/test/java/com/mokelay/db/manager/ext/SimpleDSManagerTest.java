package com.mokelay.db.manager.ext;

import com.alibaba.fastjson.JSON;
import com.mokelay.MokelayBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataPage;
import com.mokelay.base.bean.constant.CG;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.bean.view.MultiCondition;
import com.mokelay.db.manager.DSManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xiaobc on 17/7/24.
 */
public class SimpleDSManagerTest extends MokelayBaseTest {

    private DSManager dsManager;

    private List<Integer> dsIds = new ArrayList<Integer>();

    private MultiCondition condition;


    public void setUp() {
        super.setUp();
        dsManager = (DSManager) context.getBean("dsManager");
        try {
            for (int i = 0; i < 20; i++) {
                DS ds = new DS();
                ds.setAlias("" + new Random().nextInt(20));
                ds.setConnectionUrl("" + new Random().nextInt(20));
                ds.setConnectionPassword("" + new Random().nextInt(20));
                ds.setConnectionUsername("" + new Random().nextInt(20));
                ds.setName("" + new Random().nextInt(20));
                dsIds.add(dsManager.add(ds));
            }
        } catch (DBException e) {
            e.printStackTrace();
        }

        List<Condition> cons = new ArrayList<Condition>();

        Condition c = new Condition();
        c.setConditionFieldName("alias");
        c.setConditionFieldValue("" + new Random().nextInt(20));
        cons.add(c);
        Condition c2 = new Condition();
        c2.setConditionFieldName("name");
        c2.setConditionFieldValue("" + new Random().nextInt(20));
        cons.add(c2);
        condition = new MultiCondition(cons);
        condition.setCg(CG.OR);

    }

    public void tearDown() {
        if (dsIds.size() > 0) {
            try {
                for (int i = 0; i < dsIds.size(); i++) {
                    int id = dsIds.get(i);
                    dsManager.delete(id);
                }
            } catch (DBException e) {
                e.printStackTrace();
            }
            dsIds = null;
        }
    }

    public void testPage() throws DBException {
        DataPage dp = dsManager.page(1, 5, condition,"name",true);
        System.out.println("dp="+ JSON.toJSONString(dp));
    }

}