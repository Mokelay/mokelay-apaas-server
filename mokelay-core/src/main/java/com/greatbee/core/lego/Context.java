package com.greatbee.core.lego;

import com.greatbee.api.lego.Output;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.api.bean.server.APILego;
import com.greatbee.api.bean.server.OutputField;

import java.util.*;

/**
 * 上下文
 * <p/>
 * Author :CarlChen
 * Date:17/7/10
 */
public class Context {
    private Map<APILego, Output> context;

    public void addContext(APILego apiLego, Output output) {
        if (context == null) {
            context = new HashMap<APILego, Output>();
        }

        context.put(apiLego, output);
    }

    /**
     * 通过APILego的ID来获取Output
     *
     * @param apiLegoUuid
     * @return
     */
    public Output getOutput(String apiLegoUuid) {
        if (CollectionUtil.isValid(context)) {
            APILego key = null;
            Set<APILego> apiLegoSet = context.keySet();
            for (APILego apiLego : apiLegoSet) {
                if (apiLego.getUuid().equals(apiLegoUuid)) {
                    key = apiLego;
                }
            }

            if (key != null) {
                return context.get(key);
            }
        }
        return null;
    }

    public Map<APILego, Output> getContext() {
        return context;
    }

    /**
     * 获取HTTP相应列表
     *
     * @return
     */
    public List<OutputField> getResponseList() {
        List<OutputField> responseFields = new ArrayList<OutputField>();
        if (CollectionUtil.isValid(context)) {
            Collection<Output> outputs = context.values();
            if (CollectionUtil.isValid(outputs)) {
                for (Output output : outputs) {
                    responseFields.addAll(output.getResponseList());
                }
            }
        }
        return responseFields;
    }
}
