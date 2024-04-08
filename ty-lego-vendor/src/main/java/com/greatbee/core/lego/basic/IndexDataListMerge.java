package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * Index数据合并乐高
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("indexDataListMerge")
public class IndexDataListMerge implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(IndexDataListMerge.class);

    private static final String Input_Key_Index_Data_List = "index_data_list";//主list
    private static final String Input_Key_Index_Names="index_name";//id1,id2,id3,id4 //id

    private static final String Output_Key_Merged_Data_List = "merge_data_list";//返回的list

    private static final long Lego_Error_Data_List_Merge_Index_Name_Null = 300027L;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        java.util.List<String> indexList = (java.util.List<String>) input.getInputObjectValue(Input_Key_Index_Data_List);
        String indexName = input.getInputValue(Input_Key_Index_Names);//主列的名称 ，如果是多个，第一个必须是index_data_list的列名
        //获取memory 的输入字段 , 合并的列需要配置在memory下
        java.util.List<InputField> inputFields = input.getInputField(IOFT.Memory);
        //list 如果有额外的列需要加到list 需要在common中配置
        java.util.List<InputField> extraInputFields = input.getInputField(IOFT.Common);

        java.util.List list = new ArrayList<>();
        if (CollectionUtil.isValid(inputFields)) {
            if(StringUtil.isInvalid(indexName)){
                throw new LegoException("索引列名没有配置",Lego_Error_Data_List_Merge_Index_Name_Null);
            }
            String[] indexNames = null;
            if(indexName.contains(",")){
                //如果是多个主列字段
                indexNames = indexName.split(",");
            }
            for(int i=0;i<indexList.size();i++){
                String index = String.valueOf(indexList.get(i));
                Data data = new Data();
                int _index = 0;//主列序号
                String _indexName = indexNames!=null?indexNames[_index]:indexName;
                data.put(_indexName,index);
                _index++;//第一个必须是 index_data_list  列名
                for(InputField _if:inputFields){
                    Object ifItem = _if.getFieldValue();
                    if(ifItem instanceof DataList){
                        java.util.List<Data> itemList = ((DataList) ifItem).getList();
                        boolean foundIndexName = false;
                        if(CollectionUtil.isInvalid(itemList)){
                            continue;
                        }
                        for(Data _data:itemList){

                            if(_data.containsKey(_indexName) && index.equalsIgnoreCase(_data.getString(_indexName))){
                                //有这个值，并且相等时，将其他值赋给data
                                foundIndexName = true;
                                for(Map.Entry<String,Object> entry:_data.entrySet()){
                                    if(!_indexName.equalsIgnoreCase(entry.getKey())){
                                        //存到map
                                        data.put(entry.getKey(),entry.getValue());
                                    }
                                }
                                break;
                            }
                        }
                        if(!foundIndexName){
                            for(Map.Entry<String,Object> entry:itemList.get(0).entrySet()){
                                if(!_indexName.equalsIgnoreCase(entry.getKey())){
                                    //存到map
                                    data.put(entry.getKey(),0);//如果没有找到，就存0进去， TODO 后面考虑是否需要加上默认值
                                }
                            }
                        }
                    }
                    _index++;
                }
                //如果配置了额外参数，直接塞进去
                if(CollectionUtil.isValid(extraInputFields)){
                    for(InputField extraIf:extraInputFields){
                        if(Input_Key_Index_Data_List.equalsIgnoreCase(extraIf.getFieldName())||Input_Key_Index_Names.equalsIgnoreCase(extraIf.getFieldName())){
                            continue;
                        }
                        data.put(extraIf.getFieldName(),extraIf.getFieldValue());
                    }
                }
                list.add(data);
            }
        }
        //返回客户端
        output.setOutputValue(Output_Key_Merged_Data_List,list);

    }


}
