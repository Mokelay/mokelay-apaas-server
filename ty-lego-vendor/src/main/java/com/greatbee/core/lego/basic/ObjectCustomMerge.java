package com.greatbee.core.lego.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

/**
 * 对象或者数组自定义合并
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("objectCustomMerge")
public class ObjectCustomMerge implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(ObjectCustomMerge.class);

    private static final String Input_Key_Index_Data_Object = "index_data";//主对象 可以是data 也可以是list

    /**
     * json string
     * [{
     *     index_data:'uuid',//父级的条件字段
     *     sub1:'apiLegoUuid',//子集的条件字段
     *     key:'inputFields',//子集的 key
     *     isJson:false // true :sub 转换成json string
     * }]
     */
    private static final String Input_Key_Merge_Rule = "mergeRule";//合并规则

    private static final String Output_Key_Merged_Data_Object = "merge_data";//返回的list

    private static final long Lego_Error_Data_Object_Null = 300060L;
    private static final long Lego_Error_Merge_Rule_Null = 300061L;
    private static final long Lego_Error_Merge_Rule_Pattern_Error = 300062L;
    private static final long Lego_Error_Merge_Subs_Null = 300063L;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String mergeRule = input.getInputValue(Input_Key_Merge_Rule);
        if(StringUtil.isInvalid(mergeRule)){
            throw new LegoException("合并规则无效",Lego_Error_Merge_Rule_Null);
        }
        JSONArray rules = null;
        try {
            rules = JSON.parseArray(mergeRule);
        }catch(Exception e){
            throw new LegoException("合并规则格式错误！",Lego_Error_Merge_Rule_Pattern_Error);
        }
        //缓存类型中的全是需要合并的对象
        List<InputField> subs = input.getInputField(IOFT.Memory);
        if(CollectionUtil.isInvalid(subs)){
            throw new LegoException("没有需要合并的子对象",Lego_Error_Merge_Subs_Null);
        }

        Object obj = input.getInputObjectValue(Input_Key_Index_Data_Object);
        if(obj==null){
            throw new LegoException("请求参数无效",Lego_Error_Data_Object_Null);
        }
        if(obj instanceof Data || obj instanceof Map){
            Data data = (Data) obj;
            _buildChild(rules,data,subs);
        }else if(obj instanceof DataList){
            List<Data> datas = ((DataList) obj).getList();
            for(Data data:datas){
                _buildChild(rules,data,subs);
            }
        }else if(obj instanceof List){
            List<Data> datas = (List) obj;
            for(Data data:datas){
                _buildChild(rules,data,subs);
            }
        }
        //返回客户端
        output.setOutputValue(Output_Key_Merged_Data_Object,obj);
    }

    /**
     * 根据规则和主对象 从subs中合并满足规则的数据
     * @param rules
     * @param indexData
     * @param subs
     */
    private void _buildChild(JSONArray rules,Data indexData, List<InputField> subs){
        for(InputField sub:subs){
            String subName = sub.getFieldName();
            Object subVal = sub.getFieldValue();

            if(rules==null){
                return ;
            }
            for(int i=0;i<rules.size();i++){
                JSONObject rule = rules.getJSONObject(i);
                if(rule.containsKey(subName)){
                    //找到了这个sub的合并格式
                    _buildChildDetail(rule,subName,indexData,subVal);
                }
            }
        }
    }

    /**
     * 构建子项
     * @param rule
     * @param subName
     * @param indexData
     * @param subVal
     */
    private void _buildChildDetail(JSONObject rule,String subName,Data indexData, Object subVal){
        String indexKey = rule.getString(Input_Key_Index_Data_Object);
        String subKey = rule.getString(subName);
        Boolean toJsonString = rule.getBoolean("isJson");
        String returnKey = rule.getString("key");
        if(subVal instanceof Data){
            Data data = (Data) subVal;
            Data result = _checkChildItem(data,indexKey,subKey,indexData);
            if(toJsonString!=null && toJsonString){
                indexData.put(returnKey,JSON.toJSONString(result));
            }else{
                indexData.put(returnKey,result);
            }
        }else if(subVal instanceof DataList || subVal instanceof List){
            List<Data> list = null;
            if(subVal instanceof DataList){
                list = ((DataList) subVal).getList();
            }else{
                list = (List<Data>) subVal;
            }
            List<Data> resultList = new ArrayList<>();
            for(Data data:list){
                Data result = _checkChildItem(data,indexKey,subKey,indexData);
                if(result!=null){
                    resultList.add(result);
                }
            }
            if(toJsonString!=null && toJsonString){
                indexData.put(returnKey,CollectionUtil.isValid(resultList)?JSON.toJSONString(resultList):"[]");
            }else{
                indexData.put(returnKey,resultList);
            }
        }else if(subVal ==null){
            indexData.put(returnKey,null);
        }
    }

    /**
     * 判断 是否是子集
     * @param data
     * @param indexKey
     * @param subKey
     * @param indexData
     * @return
     */
    private Data _checkChildItem(Data data,String indexKey,String subKey,Data indexData){
        if(data.containsKey(subKey)&&data.get(subKey)!=null && indexData.get(indexKey)!=null
                && data.get(subKey).equals(indexData.get(indexKey))){
            return data;
        }else {
            return null;
        }
    }

}
