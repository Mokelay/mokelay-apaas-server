package com.mokelay.db.lego;

import com.mokelay.DBBaseTest;
import com.mokelay.db.ExceptionCode;

/**
 * DB 乐高测试用例基类
 * Created by usagizhang on 18/3/16.
 */
public class BaseLegoTest extends DBBaseTest implements ExceptionCode {

    public BaseLegoTest() {

    }

    /**
     * 初始化
     */
    public void setUp() {
        super.setUp("test_server.xml");
    }



}
