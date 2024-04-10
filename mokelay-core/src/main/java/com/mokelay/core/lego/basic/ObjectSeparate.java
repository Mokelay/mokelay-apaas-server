package com.mokelay.core.lego.basic;

import com.mokelay.base.bean.DataList;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * 对象分离， 将object或者list转换成单个值或者单个数组    主要和对象重组结合使用
 * <p>
 * 输入：
 * 1. common 请求参数
 * 输出：
 * 1. 配置了memory 字段时，会返回对应的单个值或者数组
 * Author :xiaobc
 * Date:17/7/20
 */
@Component("objectSeparate")
public class ObjectSeparate implements Lego,ExceptionCode {

    private static final String Input_Key_Object = "srcObject";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        Object srcObject = input.getInputObjectValue(Input_Key_Object);
        java.util.List<OutputField> ofs = output.getOutputField(IOFT.Memory);
        if(srcObject instanceof Map){
            //如果是map对象
            Map<String,Object> map = (Map<String, Object>) srcObject;
            for(OutputField of:ofs){
                String ofName = of.getFieldName();
                if(map.containsKey(ofName)){
                    of.setFieldValue(map.get(ofName));
                }
            }
        }else if(srcObject instanceof java.util.List||srcObject instanceof DataList){
            //如果是list对象
            java.util.List<Map<String,Object>> list = null;
            if(srcObject instanceof DataList){
                list = (java.util.List<Map<String, Object>>) ((DataList) srcObject).getList();
            }else {
                list = (java.util.List<Map<String, Object>>) srcObject;
            }
            for(OutputField of:ofs) {
                String ofName = of.getFieldName();
                java.util.List tempResult = new ArrayList<>();
                for(Map<String,Object> item :list){
                    if(item.containsKey(ofName)){
                        tempResult.add(item.get(ofName));
                        continue;
                    }
                }
                of.setFieldValue(tempResult);
            }
        }else{
            throw new LegoException("ObjectSeparate只支持Map和List对象",300017);
        }
    }
}
