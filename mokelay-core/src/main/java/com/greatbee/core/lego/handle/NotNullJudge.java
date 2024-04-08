package com.greatbee.core.lego.handle;

import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.FieldHandle;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 不能为空的判定
 * <p/>
 * 如果为空，则抛异常
 * <p/>
 * <p/>
 * params:
 * [0] 为提示异常信息
 * Author :CarlChen
 * Date:17/8/4
 */
@Component("notNullJudge")
public class NotNullJudge implements FieldHandle, ExceptionCode {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object obj = outputField.getFieldValue();
        boolean isNull = (LegoUtil.checkIsNotNull(obj) == false);
        if (isNull) {
            if (params != null && params.length > 0) {
                throw new LegoException(params[0], ERROR_FIELD_HANDLE_VALUE_IS_NOT_NULL);
            } else {
                throw new LegoException(outputField.getName() + "为空", ERROR_FIELD_HANDLE_VALUE_IS_NULL);
            }
        }
    }
}