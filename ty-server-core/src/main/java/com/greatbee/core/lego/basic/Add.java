package com.greatbee.core.lego.basic;

import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.constant.DBMT;
import com.greatbee.base.bean.constant.DT;
import com.greatbee.core.bean.constant.FVT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.lego.*;
import com.greatbee.core.lego.util.LogUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.db.database.DataManager;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 添加
 * <p/>
 * 输入：
 * 1. 添加字段列表(多个,ioft=create)
 * 输出：
 * 1. 添加后字段列表(多个,ioft = create)
 * 2. Output_Key_UniqueValue添加后的唯一值
 * Date: 2017/5/31
 */
@Component("add")
public class Add implements Lego, LegoGenerator, ExceptionCode {
    private static final Logger logger = Logger.getLogger(Add.class);

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

        //转成OI的Field
        java.util.List<Field> createFieldsList = new ArrayList<Field>();
        //获取需要添加的字段
        java.util.List<InputField> inputFields = input.getInputField(IOFT.Create);
        try {
            java.util.List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);
            if (CollectionUtil.isValid(oiFields)) {
                for (Field field : oiFields) {
                    InputField target = null;
                    for (InputField inputField : inputFields) {
                        if (inputField.getFieldName().equalsIgnoreCase(field.getFieldName())) {
                            target = inputField;
                            break;
                        }
                    }
                    if (target != null) {
                        field.setFieldValue(target.fieldValueToString());
                        createFieldsList.add(field);
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        if (CollectionUtil.isInvalid(createFieldsList)) {
            throw new LegoException("没有需要添加的字段", ERROR_LEGO_ADD_NO_FIELDS);
        }

        String uniqueValue;
        try {
            uniqueValue = dataManager.create(oi, createFieldsList);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_ADD);
        }

        //把添加后的唯一值写入进Output中
        output.setOutputValue(Output_Key_UniqueValue, uniqueValue);

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
        //添加日志
        LogUtil.saveLog(tyDriver, input, output, oi, "add");
    }

    @Override
    public void generate(int apiLegoId, String oiAlias) throws LegoException {
        //添加InputFields
        //添加OututFields
        try {
            java.util.List<Field> fields = tyDriver.getTyCacheService().getFields(oiAlias);
            //添加inputfields
            for(Field field:fields){
                if(field.isPk()){
                    continue;
                }
                InputField inputfield = new InputField();
                inputfield.setIft(IOFT.Create.getType());
                inputfield.setName(field.getName());
                inputfield.setFieldName(field.getFieldName());
                inputfield.setAlias(field.getFieldName());
                inputfield.setApiLegoId(apiLegoId);
                inputfield.setApiLegoUuid(DataUtil.getString(apiLegoId,""));
                inputfield.setDt(DT.String.getType());
                inputfield.setFvt(FVT.Request.getType());
                inputfield.setRequestParamName(field.getFieldName());
                inputfield.setDescription(TIP);
                tyDriver.getInputFieldManager().add(inputfield);
            }
            //添加outputFields
            OutputField of = new OutputField();
            of.setAlias(Output_Key_UniqueValue);
            of.setDescription(TIP);
            of.setApiLegoId(apiLegoId);
            of.setApiLegoUuid(DataUtil.getString(apiLegoId, ""));
            of.setName(Output_Key_UniqueValue);
            of.setFieldName(Output_Key_UniqueValue);
            of.setOft(IOFT.Common.getType());
            of.setResponse(true);
            tyDriver.getOutputFieldManager().add(of);

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_GENERATE);
        }


    }
}
