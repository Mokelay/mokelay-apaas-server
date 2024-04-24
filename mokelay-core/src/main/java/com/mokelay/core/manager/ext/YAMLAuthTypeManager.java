package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.YamlBasicManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.auth.AuthType;
import com.mokelay.core.manager.AuthTypeManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authTypeManager")
public class YAMLAuthTypeManager extends YamlBasicManager implements AuthTypeManager {

    public YAMLAuthTypeManager() {
        super(AuthType.class, DEFAULT_Mokelay_DS, "/auth_type/auth_type.yaml");
    }

    /**
     * 根据alias 查询AuthType详情
     *
     * @param alias
     * @return
     */
    @Override
    public AuthType getAuthTypeByAlias(String alias) throws DBException {
        List<AuthType> ats = list();
        if (CollectionUtil.isValid(ats)) {
            for (AuthType at : ats) {
                if (at.getAlias().equalsIgnoreCase(alias)) {
                    return at;
                }
            }
        }
        return null;
    }
}
