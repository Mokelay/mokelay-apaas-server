package com.mokelay.core.lego.handle;

import com.alibaba.fastjson.JSON;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 对象转换成json
 * <p>
 * Author :Xiaobc
 * Date:17/8/1
 */
@Component("objectToJson")
public class ObjectToJson implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();
        if(value!=null){
            //list转String
            String str = JSON.toJSONString(value);
            outputField.setFieldValue(str);
        }
    }
}
