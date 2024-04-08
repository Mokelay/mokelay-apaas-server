package com.greatbee.core.lego.handle;

import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.FieldHandle;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 数据结构判定 等于指定值报错
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
@Component("dataResultEqJudge")
public class DataResultEqJudge implements FieldHandle,ExceptionCode {

    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object obj = outputField.getFieldValue();
        boolean openException = false;
        if(NULL.equalsIgnoreCase(params[0])){
            boolean isNotNull = LegoUtil.checkIsNotNull(obj);
            if(!isNotNull){
                openException=true;
            }
        }else{
            String value = obj.toString();
            if (StringUtil.isValid(value) && value.equalsIgnoreCase(params[0])) {
                openException = true;
            }
        }
        if(openException){
            if(params!=null&&params.length>1){
                if(LegoUtil.isNumeric(params[1])){
                    throw new LegoException(params[1], DataUtil.getLong(params[1],ERROR_FIELD_HANDLE_VALUE_INVALID));
                }else {
                    throw new LegoException(params[1], ERROR_FIELD_HANDLE_VALUE_INVALID);
                }
            }else{
                throw new LegoException(outputField.getName() + "值无效", ERROR_FIELD_HANDLE_VALUE_INVALID);
            }
        }
    }
}
