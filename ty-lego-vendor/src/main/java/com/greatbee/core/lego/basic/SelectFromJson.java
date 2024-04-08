package com.greatbee.core.lego.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.CT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 根据条件从json中查询数据
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("selectFromJson")
public class SelectFromJson implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(SelectFromJson.class);

    private static final String Input_Key_Index_JSON = "index_json";//需要查询的json数据，可以是json对象也可以是jsonString

    private static final String Input_Key_Removal = "removal";//是否去重
    private static final String Input_Key_Removal_Key = "removalKey";//去重key

    private static final String Output_Key_Result_Data_Object = "result_data";//返回的list 或 对象

    private static final long Lego_Error_Find_Json_Only_Two_Level = 300065L;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        Object obj = transferJson(input.getInputObjectValue(Input_Key_Index_JSON));
        if(obj==null){
            output.setOutputValue(Output_Key_Result_Data_Object,obj);
            return;
        }
        List<InputField> conditions = input.getInputField(IOFT.Memory);
        //最后返回的对象
        //没有条件直接返回
        if(CollectionUtil.isInvalid(conditions)){
            output.setOutputValue(Output_Key_Result_Data_Object,obj);
            return;
        }
        String removal = input.getInputValue(Input_Key_Removal);//是否去重
        String removalKey = input.getInputValue(Input_Key_Removal_Key);//去重的key

        Object result = null;
        for(int i=0;i<conditions.size();i++){
            InputField condition = conditions.get(i);
            String key = condition.getFieldName();
            String ct = condition.getCt();
            Object value = condition.getFieldValue();
            //第一个条件
            if(i==0){
                if(obj instanceof JSONArray){
                    result = _buildJSONArray(key, ct, value, (JSONArray) obj);
                }else if(obj instanceof JSONObject){
                    result = _buildJSONObject(key, ct, value, (JSONObject) obj);
                }
            }else{
                if(result instanceof JSONArray){
                    obj = ((JSONArray) result).clone();
                    result = _buildJSONArray(key,ct,value, (JSONArray) obj);
//                    if(StringUtil.isValid(removal)){
//                        result = duplicateRemoval((JSONArray) result,removalKey);
//                    }
                }else if(result instanceof JSONObject){
                    obj =  ((JSONObject) result).clone();
                    result = _buildJSONObject(key, ct, value, (JSONObject) obj);
                }
            }
        }

        //返回客户端
        output.setOutputValue(Output_Key_Result_Data_Object,result);

        List<OutputField> ofs = output.getOutputField(IOFT.Memory);
        if(CollectionUtil.isValid(ofs)){
            for(OutputField _of:ofs){
                String fieldKey = _of.getFieldName();
                Object outObj = buildOutputField(result,fieldKey);
                if(outObj instanceof JSONArray && StringUtil.isValid(removal)){
                    outObj = duplicateRemoval((JSONArray) outObj,removalKey);
                }
                _of.setFieldValue(outObj);
            }
        }

    }

    /**
     * 列表去重
     * @param array
     * @return
     */
    private JSONArray duplicateRemoval(JSONArray array,String removalKey){
        if(array.size()<=0){
            return array;
        }
        JSONArray result = new JSONArray();
        for(int i=0;i<array.size();i++){
            JSONObject item = array.getJSONObject(i);
            if(item.containsKey(removalKey)){
                String key = item.getString(removalKey);
                boolean isHas = false;
                for(int j=0;j<result.size();j++){
                    if(key.equals(result.getJSONObject(j).getString(removalKey))){
                        //说明已经存在了
                        isHas =true;
                        break;
                    }
                }
                if(!isHas){
                    result.add(item);
                }
            }else{
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 构建返回数据
     * @param result
     * @param fieldKey
     * @return
     */
    private Object buildOutputField(Object result,String fieldKey){
        if(Output_Key_Result_Data_Object.equalsIgnoreCase(fieldKey)){
            //如果就是拿当前对象
            return result;
        }
        if(result instanceof JSONObject){
            return ((JSONObject) result).get(fieldKey);
        }else if(result instanceof JSONArray){
            JSONArray list = new JSONArray();
            for(int i=0;i<((JSONArray) result).size();i++){
                JSONObject item = ((JSONArray) result).getJSONObject(i);
                Object child = item.get(fieldKey);
                if(child instanceof JSONObject || child instanceof String){
                    list.add(child);
                }else if(child instanceof JSONArray && ((JSONArray) child).size()>0){
                    for(int j=0;j<((JSONArray) child).size();j++){
                        JSONObject childItem = ((JSONArray) child).getJSONObject(j);
                        list.add(childItem);
                    }
                }
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 检查是否满足条件
     * @param obj
     * @param ct
     * @param value
     * @return
     */
    private Boolean checkCondition(Object obj,String ct,Object value){
        if(obj==null){
            return false;
        }
        if(CT.EQ.getName().equalsIgnoreCase(ct)){
            //条件是等于
            if((obj instanceof Integer || obj instanceof Float || obj instanceof Double)&& Double.valueOf(String.valueOf(obj)) == Double.valueOf(String.valueOf(value))){
                return true;
            }else if(obj instanceof Boolean && ((Boolean) obj == DataUtil.getBoolean(String.valueOf(value),false))){
                return true;
            }else if(obj instanceof String && obj.equals(value)){
                return true;
            }
        }else if (CT.GE.getName().equalsIgnoreCase(ct)){
            //大于等于
            if(Double.valueOf(String.valueOf(obj)) >= Double.valueOf(String.valueOf(value))){
                return true;
            }
        }else if (CT.GT.getName().equalsIgnoreCase(ct)){
            //大于
            if(Double.valueOf(String.valueOf(obj)) > Double.valueOf(String.valueOf(value))){
                return true;
            }
        }else if (CT.LE.getName().equalsIgnoreCase(ct)){
            //小于等于
            if(Double.valueOf(String.valueOf(obj)) <= Double.valueOf(String.valueOf(value))){
                return true;
            }
        }else if (CT.LT.getName().equalsIgnoreCase(ct)){
            //小于
            if(Double.valueOf(String.valueOf(obj)) < Double.valueOf(String.valueOf(value))){
                return true;
            }
        }else if (CT.NEQ.getName().equalsIgnoreCase(ct)){
            //不等于
            if(obj instanceof String && !obj.equals(value)){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据条件筛选 json 对象
     * @param key
     * @param ct
     * @param value
     * @param item
     */
    private JSONObject _buildJSONObject(String key,String ct,Object value,JSONObject item) throws LegoException{
        JSONObject result = null;
        String[] keys = key.split("\\.");
        int keyLen = keys.length;
        int deep = 1;
        if(deep == keyLen){
            //如果等于当前长度,说明条件是   key  ct  value  eg:uuid=1
            String _key = keys[deep-1];
            Object obj = item.get(_key);
            if(checkCondition(obj,ct,value)){
                //如果满足条件
                result = item;
            }else{
                result = null;
            }
        }else{
            //继续向下找
            result = (JSONObject) item.clone();
            Object item2 = item.get(keys[deep-1]);
            deep++;
            if(item2 instanceof JSONObject){
                //如果是jsonObject 初始化if为object
                result.put(keys[deep-2],new JSONObject());
                if(deep == keyLen){
                    //找到底层
                    String _key2 = keys[deep-1];
                    Object obj2 = ((JSONObject) item2).get(_key2);
                    if(checkCondition(obj2,ct,value)){
                        //如果满足条件
                        result.put(keys[deep-2],item2);
                    }
                }else{
                    //继续向下找
                    throw new LegoException("json查找只支持二层",Lego_Error_Find_Json_Only_Two_Level);
                }
            }else if(item2 instanceof JSONArray){
                result.put(keys[deep-2],new JSONArray());
                if(deep == keyLen){
                    //找到底层
                    JSONArray childs = new JSONArray();
                    for(int j=0;j<((JSONArray) item2).size();j++){
                        JSONObject itemChild = ((JSONArray) item2).getJSONObject(j);
                        String _key2 = keys[deep-1];
                        Object obj2 =  itemChild.get(_key2);
                        if(checkCondition(obj2,ct,value)){
                            //如果满足条件
                            childs.add(itemChild);
                        }
                    }
                    result.put(keys[deep-2],childs);
                }else{
                    //继续向下找
                    throw new LegoException("json查找只支持二层",Lego_Error_Find_Json_Only_Two_Level);
                }
            }
        }
        return result;
    }

    /**
     * 先最多三层吧，后面有需求再优化
     * 根据条件筛选 json 数组
     * @param array
     */
    private JSONArray _buildJSONArray(String key,String ct,Object value,JSONArray array) throws LegoException{
        JSONArray result = new JSONArray();
        for(int i=0;i<array.size();i++){
            JSONObject obj = _buildJSONObject(key, ct, value, array.getJSONObject(i));
            if(obj!=null){
                result.add(obj);
            }
        }
        return result;
    }




    /**
     * DataList  和 list  先不考虑
     * 将对象转换成json对象
     * @param obj
     * @return
     */
    private Object transferJson(Object obj){
        if(obj==null){
            return null;
        }
        if(obj instanceof DataList){
            return ((DataList) obj).getList();
        }else if(obj instanceof List || obj instanceof JSONObject || obj instanceof JSONArray || obj instanceof Data){
            return obj;
        }else if(obj instanceof String){
            Object o = null;
            try{
                o = JSON.parse((String) obj);
            }catch(Exception e){
                try {
                    o = JSON.parseArray((String) obj);
                }catch(Exception e2){
                    o = null;
                }
            }
            return o;
        }else {
            return null;
        }
    }


}
