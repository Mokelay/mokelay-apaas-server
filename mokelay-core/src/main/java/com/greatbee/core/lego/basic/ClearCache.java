package com.greatbee.core.lego.basic;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYCacheService;
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
