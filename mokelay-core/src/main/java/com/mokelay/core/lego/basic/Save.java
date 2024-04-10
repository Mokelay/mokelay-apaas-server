package com.mokelay.core.lego.basic;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.constant.DBMT;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.db.database.DataManager;
import com.mokelay.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 添加
 * <p>
 * 输入：
 * 1. 添加字段列表(多个,ioft=create)
 * 2. 更新字段列表(多个,ioft=update) 主键也是传进来的
 * 3. 需要更新的条件(多个 ioft = condition)
 * 输出：
 * 1. 添加后字段列表(多个,ioft = create)
 * 2. Output_Key_UniqueValue添加后的唯一值
 * Date: 2017/5/31
 */
@Component("save")
public class Save implements Lego, ExceptionCode {
    private static final Logger logger = Logger.getLogger(Save.class);

    @Autowired
    private TYDriver tyDriver;

    private static final String Output_Key_UniqueValue = "unique_value";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        ApplicationContext wac = SpringContextUtil.getApplicationContext();

        String oiAlias = input.getApiLego().getOiAlias();

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

        //通过DST获取DataManager
        DBMT dbmt = DBMT.getDBMT(ds.getDst());
        if (dbmt == null) {
            throw new LegoException(ds.getDst() + "数据源不支持", ERROR_DB_DST_NOT_SUPPORT);
        }
        DataManager dataManager = (DataManager) wac.getBean(dbmt.getType());

        try {
            java.util.List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);
            //获取需要添加的字段
            java.util.List<InputField> inputFields = input.getInputField(IOFT.Create);
            java.util.List<InputField> updateInputFields = input.getInputField(IOFT.Update);

            java.util.List<Field> createFieldsList = new ArrayList<Field>();

            Field pkField = null;//主键字段
            java.util.List<Field> updateFields = new ArrayList<Field>();
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
                if (field.isPk() && updatetTrget != null && updatetTrget.getFieldValue() != null && !"".equals(updatetTrget.getFieldValue())) {
                    pkField = (Field) field.clone();
                    pkField.setFieldValue(DataUtil.getString(updatetTrget.getFieldValue(), ""));
                    isUpdate = true;
                }

                //针对从上一个节点或者http请求获取的数据填充
                if (target != null && target.getFieldValue() != null) {
                    Field newField = (Field) field.clone();
                    newField.setFieldValue(DataUtil.getString(target.getFieldValue(), ""));
                    createFieldsList.add(newField);
                }
                if (updatetTrget != null && updatetTrget.getFieldValue() != null) {
                    Field newField = (Field) field.clone();
                    newField.setFieldValue(DataUtil.getString(updatetTrget.getFieldValue(), ""));
                    updateFields.add(newField);
                }

            }
            if (isUpdate) {
                //更新
                if (CollectionUtil.isValid(updateFields) && pkField != null) {
                    dataManager.update(oi, updateFields, pkField);
                } else {
                    throw new LegoException("没有需要更新的字段", ERROR_LEGO_UPDATE_NO_FIELDS);
                }
            } else {
                //添加
                if (CollectionUtil.isValid(createFieldsList)) {
                    String uniqueValue = dataManager.create(oi, createFieldsList);
                    //把添加后的唯一值写入进Output中
                    output.setOutputValue(Output_Key_UniqueValue, uniqueValue);
                } else {
                    throw new LegoException("没有需要添加的字段", ERROR_LEGO_ADD_NO_FIELDS);
                }
            }


            //如果output中配置了表单字段，则把Create字段全部写入到output中去
            java.util.List<OutputField> outputFieldList = output.getOutputField(IOFT.Create);
            if (CollectionUtil.isValid(outputFieldList)) {
                for (OutputField outputField : outputFieldList) {
                    Object value = input.getInputObjectValue(outputField.getFieldName());
                    if (value != null) {
                        output.setOutputValue(outputField.getFieldName(), value);
                    }
                }
            }
            //如果output中配置了表单字段，则把Create字段全部写入到output中去
            java.util.List<OutputField> outputUpdateFieldList = output.getOutputField(IOFT.Update);
            if (CollectionUtil.isValid(outputUpdateFieldList)) {
                for (OutputField outputField : outputUpdateFieldList) {
                    Object value = input.getInputObjectValue(outputField.getFieldName());
                    if (value != null) {
                        output.setOutputValue(outputField.getFieldName(), value);
                    }
                }
            }

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        //添加日志
        //LogUtil.saveLog(tyDriver, input, output, oi, "save");
    }
}
