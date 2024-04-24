package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.core.bean.server.API;
import com.mokelay.core.bean.view.APIView;
import com.mokelay.core.manager.APIManager;
import org.springframework.stereotype.Component;

@Component("apiViewManager")
public class YamlAPIViewManager extends YamlBasicManager implements APIManager {
    public YamlAPIViewManager() {
        super(APIView.class, DEFAULT_Mokelay_DS, "/api");
    }

    @Override
    public API getAPIByAlias(String alias) throws DBException {
        return null;
    }
}
