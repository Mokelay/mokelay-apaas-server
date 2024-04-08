package com.greatbee.core.lego.basic.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.constant.*;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.view.Condition;
import com.greatbee.db.bean.view.ConnectorTree;
import com.greatbee.db.bean.view.MultiCondition;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.BuildConnectorTreeUtils;
import com.greatbee.core.manager.TYDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Read
 * <p/>
 * Author :CarlChen
 * Date:17/7/31
 */
public class BaseRead implements ExceptionCode {
    protected static final String Input_Key_Order_Field = "order_field";
    protected static final String Input_Key_Order = "order";
    protected static final String Input_Key_Group_Field = "group_field";
    protected static final String Input_Key_Keywords = "keywords";
    protected static final String Input_Key_Global_Search_Fields = "global_search_fields";

    protected static final String Output_Key_Data_List = "data_list";

    protected static final String Input_Key_Page = "page";
    protected static final String Input_Key_PageSize = "pageSize";
    protected static final String OUTPUT_KEY_PAGE_DATA = "page_data";

    protected static final String GLOBAL_SEARCH_FIELDS_NAME="全局搜索字段(表单)";
    protected static final String KEYWORDS_NAME="关键字搜索";
    protected static final String GROUP_FIELD_NAME="群组字段";
    protected static final String ORDER_NAME = "排序";
    protected static final String ORDER_FIELD_NAME="排序字段";
    protected static final String PAGE_NAME="请求页码";
    protected static final String PAGE_SIZE_NAME="每页条数";
    protected static final String DATA_LIST_NAME = "列表数据";
    protected static final String PAGE_DATA_NAME = "分页数据";


    /**
     * 根据input 构建connectorTree
     *
     * @param tyDriver
     * @param input
     * @throws LegoException
     */
    protected ConnectorTree buildConnectorTree(TYDriver tyDriver, Input input) throws LegoException {
       return buildConnectorTree(tyDriver,input,false);
    }
    /**
     * 根据input 构建connectorTree
     *
     * @param tyDriver
     * @param input
     * @throws LegoException
     */
    protected ConnectorTree buildConnectorTree(TYDriver tyDriver, Input input,Boolean noRead) throws LegoException {
        return BuildConnectorTreeUtils.buildConnectorTree(tyDriver, input, noRead);
    }

    /**
     * 构建list和page的多字段返回参数
     *
     * @param output
     * @param list
     */
    protected void buildOutputFields(Output output, List list) {
        List<OutputField> outputFields = output.getOutputField(IOFT.Read);
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField outputField : outputFields) {
                List datas = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Data data = (Data) list.get(i);
                    Object outputFieldValue = data.get(outputField.getFieldName());//这里拿outputField的fieldName，这里输出的fieldName和输入的alias一样
                    if (outputFieldValue != null) {//解决如果是 outputField.getAlias() 是data，就会返回null
                        datas.add(outputFieldValue);
                    }
                }
                outputField.setFieldValue(datas);
            }
        }
    }

    /**
     * 构建list和page的全局搜索字段
     * @param input
     * @param root
     */
    protected  void buildKeywords(Input input,ConnectorTree root){
        //处理全局搜索
        //全局搜索关键字
        String keywords = input.getInputValue(Input_Key_Keywords);
        //需要全局搜索的字段，例如["name","description","label"]，为当前表root里的字段
        String globalSearchFieldsStr = input.getInputValue(Input_Key_Global_Search_Fields);
        if (StringUtil.isValid(keywords) && StringUtil.isValid(globalSearchFieldsStr)) {
            JSONArray globalSearchFields = JSON.parseArray(globalSearchFieldsStr);
            MultiCondition gsc = new MultiCondition();
            gsc.setCg(CG.OR);
            for (Object searchField : globalSearchFields) {
                Condition c = new Condition();
                c.setConditionFieldName((String) searchField);
                c.setConditionFieldValue(keywords);
                c.setCt(CT.LIKE.getName());
                gsc.addCondition(c);
            }
            root.addCondition(gsc);
        }
    }

    /**
     * 公共的代码提到父类中，构建一键生成接口  inputFIelds  和 outputFields
     * @param tyDriver
     * @param fields
     * @param apiLegoId
     * @param isList
     * @throws DBException
     */
    protected void buildListAndPageGenerate(TYDriver tyDriver, List<Field> fields, int apiLegoId, boolean isList) throws DBException {
        for(Field field:fields){
            if(field.isPk()){
                //默认 主键全局搜索
                InputField inputfield = new InputField();
                inputfield.setIft(IOFT.Common.getType());
                inputfield.setName(GLOBAL_SEARCH_FIELDS_NAME);
                inputfield.setFieldName(Input_Key_Global_Search_Fields);
                inputfield.setAlias(Input_Key_Global_Search_Fields);
                inputfield.setApiLegoId(apiLegoId);
                inputfield.setDt(DT.String.getType());
                inputfield.setFvt(FVT.Constant.getType());
                inputfield.setConstant("[\""+field.getFieldName()+"\"]");
                inputfield.setDescription(TIP);
                tyDriver.getInputFieldManager().add(inputfield);
            }
            InputField inputfield = new InputField();
            inputfield.setIft(IOFT.Read.getType());
            inputfield.setName(field.getName());
            inputfield.setFieldName(field.getFieldName());
            inputfield.setAlias(field.getFieldName());
            inputfield.setApiLegoId(apiLegoId);
            inputfield.setDt(DT.String.getType());
            inputfield.setDescription(TIP);
            tyDriver.getInputFieldManager().add(inputfield);
        }
        //keywords
        InputField inputfield = new InputField();
        inputfield.setIft(IOFT.Common.getType());
        inputfield.setName(KEYWORDS_NAME);
        inputfield.setFieldName(Input_Key_Keywords);
        inputfield.setAlias(Input_Key_Keywords);
        inputfield.setApiLegoId(apiLegoId);
        inputfield.setDt(DT.String.getType());
        inputfield.setFvt(FVT.Request.getType());
        inputfield.setRequestParamName(Input_Key_Keywords);
        inputfield.setDescription(TIP);
        tyDriver.getInputFieldManager().add(inputfield);

        //group
        InputField groupInputfield = new InputField();
        groupInputfield.setIft(IOFT.Common.getType());
        groupInputfield.setName(GROUP_FIELD_NAME);
        groupInputfield.setFieldName(Input_Key_Group_Field);
        groupInputfield.setAlias(Input_Key_Group_Field);
        groupInputfield.setApiLegoId(apiLegoId);
        groupInputfield.setDt(DT.String.getType());
        groupInputfield.setDescription(TIP);
        tyDriver.getInputFieldManager().add(groupInputfield);

        //order
        InputField orderInputfield = new InputField();
        orderInputfield.setIft(IOFT.Common.getType());
        orderInputfield.setName(ORDER_NAME);
        orderInputfield.setFieldName(Input_Key_Order);
        orderInputfield.setAlias(Input_Key_Order);
        orderInputfield.setApiLegoId(apiLegoId);
        orderInputfield.setDt(DT.String.getType());
        orderInputfield.setDescription(TIP);
        tyDriver.getInputFieldManager().add(orderInputfield);

        //order field
        InputField orderFieldInputfield = new InputField();
        orderFieldInputfield.setIft(IOFT.Common.getType());
        orderFieldInputfield.setName(ORDER_FIELD_NAME);
        orderFieldInputfield.setFieldName(Input_Key_Order_Field);
        orderFieldInputfield.setAlias(Input_Key_Order_Field);
        orderFieldInputfield.setApiLegoId(apiLegoId);
        orderFieldInputfield.setDt(DT.String.getType());
        orderFieldInputfield.setDescription(TIP);
        tyDriver.getInputFieldManager().add(orderFieldInputfield);

        if(isList){
            //添加outputFields
            OutputField of = new OutputField();
            of.setAlias(Output_Key_Data_List);
            of.setDescription(TIP);
            of.setApiLegoId(apiLegoId);
            of.setName(DATA_LIST_NAME);
            of.setFieldName(Output_Key_Data_List);
            of.setOft(IOFT.Common.getType());
            of.setResponse(true);
            tyDriver.getOutputFieldManager().add(of);
        }else{
            //page
            InputField pageInputfield = new InputField();
            pageInputfield.setIft(IOFT.Common.getType());
            pageInputfield.setName(PAGE_NAME);
            pageInputfield.setFieldName(Input_Key_Page);
            pageInputfield.setAlias(Input_Key_Page);
            pageInputfield.setApiLegoId(apiLegoId);
            pageInputfield.setDt(DT.String.getType());
            pageInputfield.setFvt(FVT.Request.getType());
            pageInputfield.setRequestParamName(Input_Key_Page);
            pageInputfield.setDescription(TIP);
            tyDriver.getInputFieldManager().add(pageInputfield);

            //pageSize
            InputField pageSizeInputfield = new InputField();
            pageSizeInputfield.setIft(IOFT.Common.getType());
            pageSizeInputfield.setName(PAGE_SIZE_NAME);
            pageSizeInputfield.setFieldName(Input_Key_PageSize);
            pageSizeInputfield.setAlias(Input_Key_PageSize);
            pageSizeInputfield.setApiLegoId(apiLegoId);
            pageSizeInputfield.setDt(DT.String.getType());
            pageSizeInputfield.setFvt(FVT.Request.getType());
            pageSizeInputfield.setRequestParamName(Input_Key_PageSize);
            pageSizeInputfield.setDescription(TIP);
            tyDriver.getInputFieldManager().add(pageSizeInputfield);

            //添加outputFields
            OutputField of = new OutputField();
            of.setAlias(OUTPUT_KEY_PAGE_DATA);
            of.setDescription(TIP);
            of.setApiLegoId(apiLegoId);
            of.setName(PAGE_DATA_NAME);
            of.setFieldName(OUTPUT_KEY_PAGE_DATA);
            of.setOft(IOFT.Common.getType());
            of.setResponse(true);
            tyDriver.getOutputFieldManager().add(of);
        }


    }

}
