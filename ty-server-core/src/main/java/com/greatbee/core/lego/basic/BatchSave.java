package com.greatbee.core.lego.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.constant.DBMT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LogUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.db.database.DataManager;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量 添加  更新（仅根据主键来更新）
 * <p>
 * 输入：
 * 1. 添加字段列表(多个,ioft=create)
 * 输出：
 * 1. 添加后字段列表(多个,ioft = create)
 * 2. Output_Key_UniqueValue添加后的唯一值
 * Date: 2017/5/31
 */
@Component("batchSave")
public class BatchSave implements Lego, ExceptionCode {
    private static final Logger logger = Logger.getLogger(BatchSave.class);

    @Autowired
    private TYDriver tyDriver;

    private static final String Input_Key_List_Param = "list_Param";

    private static final String Output_Key_List_UniqueValue = "list_unique_value";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        ApplicationContext wac = SpringContextUtil.getApplicationContext();

        String oiAlias = input.getApiLego().getOiAlias();
        InputField listParam = input.getInputField(Input_Key_List_Param);
        if(listParam.fieldValueToString()==null){
            return;
        }
        JSONArray ja = null;
        try {
           ja= JSONArray.parseArray(listParam.fieldValueToString());
        }catch(Exception e){
            e.printStackTrace();
            throw new LegoException("请求参数必须是有效的jsonString格式",300003);
        }

        //通过 oiAlias获取OI
        OI oi;
        try {
            oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        //通过oi.dsAlias获取DS
        DS ds;
        try {
            ds = tyDriver.getTyCacheService().getDSByAlias(oi.getDsAlias());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        //通过DST获取DataManager7
        DBMT dbmt = DBMT.getDBMT(ds.getDst());
        if (dbmt == null) {
            throw new LegoException(ds.getDst() + "数据源不支持", ERROR_DB_DST_NOT_SUPPORT);
        }
        DataManager dataManager = (DataManager) wac.getBean(dbmt.getType());

        //转成OI的Field
        List<List<Field>> createList = new ArrayList<List<Field>>();

        List<UpdateBean> updateBeans = new ArrayList<UpdateBean>();


        //获取需要添加的字段
        List<InputField> inputFields = input.getInputField(IOFT.Create);
        List<InputField> updateInputFields = input.getInputField(IOFT.Update);
        try {
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);
            if (CollectionUtil.isValid(oiFields)) {

                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jobj = ja.getJSONObject(i);
                    List<Field> createFieldsList = new ArrayList<Field>();

                    Field pkField = null;//主键字段
                    List<Field> updateFields = new ArrayList<Field>();
                    boolean isUpdate = false;

                    for (Field field : oiFields) {
                        InputField target = null;
                        InputField updatetTrget = null;
                        //创建字段
                        for (InputField inputField : inputFields) {
                            if (inputField.getFieldName().equalsIgnoreCase(field.getFieldName())) {
                                target = inputField;
                                break;
                            }
                        }
                        //更新字段
                        for (InputField inputField : updateInputFields) {
                            if (inputField.getFieldName().equalsIgnoreCase(field.getFieldName())) {
                                updatetTrget = inputField;
                                break;
                            }
                        }
                        //根据主键 更新
                        if (field.isPk()) {
                            pkField = (Field) field.clone();
                        }

                        for (String str : jobj.keySet()) {
                            if (pkField != null && pkField.getFieldName().equalsIgnoreCase(str) && StringUtil.isValid(jobj.getString(str))) {
                                //主键字段
                                pkField.setFieldValue(jobj.getString(str));
                                isUpdate = true;
                            }
                            //创建
                            if (target != null && target.getFieldName().equals(str)) {
                                //如果create字段 名  和 传进来的数组key相同，取其value
                                Field newField = (Field) field.clone();
                                newField.setFieldValue(jobj.getString(str));
                                //设置inputfiled的value，方便后面返回create组的字段值
                                if(StringUtil.isInvalid(jobj.getString(str))&&target.getFieldValue() != null&&!target.getFieldValue().equals("")){
                                    newField.setFieldValue(DataUtil.getString(target.getFieldValue(),""));
                                }
//                                else{
//                                    target.setFieldValue(jobj.getString(str));
//                                }
                                createFieldsList.add(newField);
                            }
                            //更新
                            if (updatetTrget != null && updatetTrget.getFieldName().equals(str)) {
                                //如果update字段 名  和 传进来的数组key相同，取其value
                                Field newField = (Field) field.clone();
                                newField.setFieldValue(jobj.getString(str));
                                //设置inputfiled的value，方便后面返回update组的字段值
                                updatetTrget.setFieldValue(jobj.getString(str));
                                updateFields.add(newField);
                            }
                        }
                        //针对从上一个节点或者http请求获取的数据填充
                        if (target != null && target.getFieldValue() != null&&!target.getFieldValue().equals("")&&!checkRepeat(createFieldsList,field.getFieldName())) {
                            Field newField = (Field) field.clone();
                            newField.setFieldValue(DataUtil.getString(target.getFieldValue(), ""));
                            createFieldsList.add(newField);
                        }
                        if (updatetTrget != null && updatetTrget.getFieldValue() != null&&!updatetTrget.getFieldValue().equals("")&&!checkRepeat(updateFields,field.getFieldName())) {
                            Field newField = (Field) field.clone();
                            newField.setFieldValue(DataUtil.getString(updatetTrget.getFieldValue(), ""));
                            updateFields.add(newField);
                        }

                    }
                    if (CollectionUtil.isValid(createFieldsList) && !isUpdate) {
                        createList.add(createFieldsList);
                    }
                    if (CollectionUtil.isValid(updateFields) && pkField != null && isUpdate) {
                        UpdateBean ub = new UpdateBean();
                        ub.setPkField(pkField);
                        ub.setFields(updateFields);
                        updateBeans.add(ub);
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        if (CollectionUtil.isInvalid(createList) && CollectionUtil.isInvalid(updateBeans)) {
//            throw new LegoException("没有需要添加或者更新的字段", ERROR_LEGO_ADD_NO_FIELDS);
            logger.info("[BatchSave] 没有需要添加或者更新的字段");
            return ;
        }

        List<String> uniqueValueList = new ArrayList<String>();
        try {
            //创建   如果是更新就没有返回唯一值
            for (List<Field> fl : createList) {
                String uniqueValue = dataManager.create(oi, fl);
                uniqueValueList.add(uniqueValue);
            }
            //更新
            for (UpdateBean ub : updateBeans) {
                dataManager.update(oi, ub.getFields(), ub.getPkField());
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_ADD);
        }

        //把添加后的唯一值写入进Output中
        output.setOutputValue(Output_Key_List_UniqueValue, uniqueValueList);


        if (CollectionUtil.isValid(createList)) {
            //字段组返回
            this.buildOutputFields(IOFT.Create, output, createList);
        }
        if (updateBeans != null && updateBeans.size() > 0) {
            //字段组返回
            this.buildOutputFields(IOFT.Update, output, updateBeans);
        }

        //添加日志
        LogUtil.saveLog(tyDriver, input, output, oi, "batchSave");

    }

    /**
     * 校验list中是否存在某个字段
     * @param fields
     * @param fieldName
     * @return
     */
    private boolean checkRepeat(List<Field> fields,String fieldName){
        for(Field f:fields){
            if(f.getFieldName().equalsIgnoreCase(fieldName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 构建list和page的多字段返回参数
     *
     * @param output
     * @param list
     */
    private void buildOutputFields(IOFT ioft, Output output, List list) {
        List<OutputField> outputFields = output.getOutputField(ioft);
        if (CollectionUtil.isValid(outputFields)) {
            for (OutputField outputField : outputFields) {
                List datas = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    List<Field> row = null;
                    if (list.get(i) instanceof UpdateBean) {
                        row = ((UpdateBean) list.get(i)).getFields();
                    } else {
                        row = (List<Field>) list.get(i);
                    }
                    for (Field f : row) {
                        if (f.getFieldName().equalsIgnoreCase(outputField.getFieldName())) {
                            String outputFieldValue = f.getFieldValue();
                            datas.add(outputFieldValue);
                            break;
                        }
                    }
                }
                outputField.setFieldValue(datas);
            }
        }
    }

    class UpdateBean {
        private Field pkField;//主键字段
        private List<Field> fields;//需要更新的字段

        public Field getPkField() {
            return pkField;
        }

        public void setPkField(Field pkField) {
            this.pkField = pkField;
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }
    }
}
