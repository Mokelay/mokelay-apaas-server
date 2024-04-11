package com.mokelay.core.lego.handle;

import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.util.LegoUtil;
import org.springframework.stereotype.Component;

/**
 * 数据结构判定 不等于指定值报错
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
@Component("dataResultJudge")
public class DataResultJudge implements FieldHandle,ExceptionCode {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object obj = outputField.getFieldValue();
        boolean openException = false;
        if(NULL.equalsIgnoreCase(params[0])){
            boolean isNotNull = LegoUtil.checkIsNotNull(obj);
            if(isNotNull){
                openException=true;
            }
        }else{
            String value = obj.toString();
            if (StringUtil.isValid(value) && !value.equalsIgnoreCase(params[0])) {
                openException = true;
            }
        }

        if (openException) {
            if(params!=null&&params.length>1){
                if(LegoUtil.isNumeric(params[1])){
                    throw new LegoException(params[1], DataUtil.getLong(params[1], ERROR_FIELD_HANDLE_VALUE_INVALID));
                }else {
                    throw new LegoException(params[1], ERROR_FIELD_HANDLE_VALUE_INVALID);
                }
            }else{
                throw new LegoException(outputField.getName() + "不是指定值", ERROR_FIELD_HANDLE_VALUE_INVALID);
            }
        }
    }
}
