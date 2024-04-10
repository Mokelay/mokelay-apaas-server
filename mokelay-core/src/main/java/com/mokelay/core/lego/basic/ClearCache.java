package com.mokelay.core.lego.basic;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.basic.ext.BaseRead;
import com.mokelay.core.manager.TYCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 清除TY内置缓存
 * <p/>
 * 输入：
 * 输出：
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("clearCache")
public class ClearCache extends BaseRead implements Lego {

    @Autowired
    private TYCacheService tyCacheService;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //清除所有ty cache
        tyCacheService.clearAllCache();
    }
}
