package com.mokelay.core.manager.ext;

import com.mokelay.MokelayBaseTest;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.auth.AuthType;
import com.mokelay.core.manager.AuthTypeManager;
import junit.framework.TestCase;

import java.util.List;

public class YamlAuthTypeManagerTest extends MokelayBaseTest {

    public void testGetAuthTypeByAlias() throws DBException {
        AuthTypeManager authTypeManager = (AuthTypeManager) context.getBean("authTypeManager");

        String atAlais = "ty_b_session";
        AuthType at = authTypeManager.getAuthTypeByAlias(atAlais);
        assertEquals(at.getAlias(), atAlais);
        System.out.println(at.getJudgeAPIAlias());
    }

    public void testGetAuthTypes() throws DBException {
        AuthTypeManager authTypeManager = (AuthTypeManager) context.getBean("authTypeManager");
        List<AuthType> ats = authTypeManager.list();
        assertEquals(ats.size(), 3);
        if (CollectionUtil.isValid(ats)) {
            for (AuthType at : ats) {
                System.out.println(at.getAlias());
            }
        }
    }
}