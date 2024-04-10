package com.mokelay.core.lego.handle;

import com.mokelay.base.util.DataUtil;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * Boolean转换
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
@Component("booleanFormat")
public class BooleanFormat implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();
        if(value instanceof Boolean){
            outputField.setFieldValue(DataUtil.getBoolean(value,false)?"是":"否");
        }else if(value instanceof Integer || value instanceof String){
            outputField.setFieldValue((DataUtil.getInt(value,0)==1)?"是":"否");
        }
    }
}
