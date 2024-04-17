package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicYAMLManager;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.auth.AuthType;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.manager.AuthTypeManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authTypeManager")
public class YAMLAuthTypeManager extends BasicYAMLManager implements AuthTypeManager {
    /**
     * 根据alias 查询AuthType详情
     *
     * @param alias
     * @return
     */
    @Override
    public AuthType getAuthTypeByAlias(String alias) {
        List<AuthType> ats = getAuthTypes();
        if (CollectionUtil.isValid(ats)) {
            for (AuthType at : ats) {
                if (at.getAlias().equalsIgnoreCase(alias)) {
                    return at;
                }
            }
        }
        return null;
    }

    /**
     * 获取AuthType列表
     *
     * @return
     */
    @Override
    public List<AuthType> getAuthTypes() {
        return _list(AuthType.class, "/auth_type/auth_type.yaml");
    }
}
