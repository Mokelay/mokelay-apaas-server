package com.mokelay.core.lego.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 请求结果大小计算
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
@Component("resultSizeComputed")
public class ResultSizeComputed implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();
        try {
            String str = JSONObject.toJSONString(value);
            int len = str.getBytes("UTF-8").length;
            Object obj = outputField.getFieldValue();
            if(obj!=null){
                JSONObject json = (JSONObject) JSON.toJSON(obj);
                json.put("size",len);
                outputField.setFieldValue(json);
            }
        }catch(Exception e){

        }
    }
}
