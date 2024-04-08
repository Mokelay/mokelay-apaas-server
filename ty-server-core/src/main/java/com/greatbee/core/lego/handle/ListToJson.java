package com.greatbee.core.lego.handle;

import com.alibaba.fastjson.JSONArray;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.FieldHandle;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 列表转json String   参数是返回的json去除某几个字段的值,比如去除id值
 * <p>
 * Author :CarlChen
 * Date:17/8/1
 */
@Component("listToJson")
public class ListToJson implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();
        List<Data> list = null;
        if(value instanceof DataPage){
            //分页数据
            DataPage dp = (DataPage) value;
              list= dp.getCurrentRecords();
        }else if(value instanceof DataList){
            DataList dl = (DataList) value;
            list = dl.getList();
        }
        if(params!=null&&params.length>0){
            for(Data data:list){
                for(int i=0;i<params.length;i++){
                    if(data.containsKey(params[i])){
                        data.put(params[i],"");
                    }
                }
            }
        }
        //list转String
        String str = JSONArray.toJSONString(list);
        outputField.setFieldValue(str);
    }
}
