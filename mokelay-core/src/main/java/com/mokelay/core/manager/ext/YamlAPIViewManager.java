package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.view.APIView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("apiViewManager")
public class YamlAPIViewManager extends YamlBasicManager {
    public YamlAPIViewManager() {
        super(APIView.class, DEFAULT_Mokelay_DS, "/api");
    }

    /**
     * Get APIView By Alias
     *
     * @param alias
     * @return
     * @throws DBException
     */
    public APIView getAPIViewByAlias(String alias) throws DBException {
        List<APIView> apiViewList = this.list();
        if (CollectionUtil.isValid(apiViewList)) {
            for (APIView apiView : apiViewList) {
                if (apiView.getApi().getAlias().equalsIgnoreCase(alias)) {
                    return apiView;
                }
            }
        }
        return null;
    }
}
