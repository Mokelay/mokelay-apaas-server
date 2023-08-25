package com.greatbee.core.lego.handle;

import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.FieldHandle;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 数据为空的判定  当数据为不为null时抛错
 * <p/>
 * 如果为空，则抛异常
 * <p/>
 * Author :CarlChen
 * Date:17/8/4
 */
@Component("nullJudge")
public class NullJudge implements FieldHandle, ExceptionCode {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object obj = outputField.getFieldValue();
        boolean isNotNull = LegoUtil.checkIsNotNull(obj);
        if (isNotNull) {
            if(params!=null&&params.length>0){
                throw new LegoException(params[0], ERROR_FIELD_HANDLE_VALUE_IS_NOT_NULL);
            }else{
                throw new LegoException(outputField.getName() + "不能为空", ERROR_FIELD_HANDLE_VALUE_IS_NOT_NULL);
            }
        }
    }
}
