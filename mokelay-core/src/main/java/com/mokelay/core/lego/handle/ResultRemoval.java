package com.mokelay.core.lego.handle;

import com.mokelay.base.bean.Data;
import com.mokelay.base.bean.DataList;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 结果去重
 * <p>
 * Author :xiaobc
 * Date:17/8/1
 */
@Component("resultRemoval")
public class ResultRemoval implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        Object value = outputField.getFieldValue();
        try {
            if (params==null || params.length<=0||value==null) {
                return;
            }
            Map<String,Integer> map = new HashMap<>();
            if (value instanceof DataList) {
                DataList dl = (DataList) value;
                List<Data> list = dl.getList();
                Iterator<Data> itList = list.iterator();
                while(itList.hasNext()){
                    Data data = itList.next();
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<params.length;i++){
                        if(data.containsKey(params[i])){
                            sb.append(data.getString(params[i]));
                        }
                    }
                    if(map.containsKey(sb.toString())){
                        //如果已经存在这条记录了，就删除
                        itList.remove();
                    }else{
                        map.put(sb.toString(),1);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}