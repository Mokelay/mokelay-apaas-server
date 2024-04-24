package com.mokelay.core.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.bean.view.OIView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("dsViewManager")
public class YamlDSViewManager extends YamlBasicManager {
    public YamlDSViewManager() {
        super(DSView.class, DEFAULT_Mokelay_DS, "/ds");
    }

    /**
     * 根据ds的alias 查询DS详情
     *
     * @param alias
     * @return
     */
    public DSView getDSViewByAlias(String alias) throws DBException {
        List<DSView> dsViewList = this.list();
        for (DSView dsView : dsViewList) {
            if (dsView.getDs().getAlias().equalsIgnoreCase(alias)) {
                return dsView;
            }
        }
        return null;
    }

    public OIView getOIViewByAlias(String oiAlias) {
        return null;
    }
}
