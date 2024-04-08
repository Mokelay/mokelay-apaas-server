package com.greatbee.core.lego.basic;

import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.constant.CT;
import com.greatbee.base.bean.constant.DT;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.constant.*;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.bean.oi.Field;
import com.greatbee.db.bean.oi.OI;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.lego.*;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.lego.util.LogUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.db.database.DataManager;
import com.greatbee.api.util.SpringContextUtil;
import com.greatbee.db.bean.constant.DBMT;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新
 * <p/>
 * 输入：
 * 1. 更新的字段列表(多个 ioft = update)
 * 2. 需要更新的条件(多个 ioft = condition)
 * 输出：
 * 1. 更新后的OI(多个 ioft = update)
 * 2. Output_Key_UniqueValue添加后的唯一值
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("update")
public class Update implements Lego,LegoGenerator, ExceptionCode {
    private static final Logger logger = Logger.getLogger(Update.class);
//    private static final String Output_Key_UniqueValue = "unique_value";

    // 如果设置了noFieldSkip 值，表示 没有更新的字段不跑错
    private static final String Input_Key_No_Field_Skip = "noFieldSkip";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        ApplicationContext wac = SpringContextUtil.getApplicationContext();

        String oiAlias = input.getApiLego().getOiAlias();

        String noFieldSkip = input.getInputValue(Input_Key_No_Field_Skip);

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
        String dst = ds.getDst();
        DataManager dataManager = (DataManager) wac.getBean(DBMT.Mysql.getType());

        //转成OI的Field
        List<Field> updateFieldsList = new ArrayList<Field>();

        //获取需要添加的字段
        List<InputField> inputFields = input.getInputField(IOFT.Update);
        try {
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);
            if (CollectionUtil.isValid(oiFields)) {
                for (Field field : oiFields) {
                    InputField _inputField = null;
                    for (InputField inputField : inputFields) {
                        if (inputField.getFieldName().equalsIgnoreCase(field.getFieldName())) {
                            _inputField = inputField;
                            break;
                        }
                    }
                    if (_inputField != null) {
                        FVT fvt = FVT.getFVT(_inputField.getFvt());
                        if (fvt == null) {
                            fvt = FVT.Constant;
                        }
                        if(FVT.Request.equals(fvt)){
                            //只屏蔽request类型的
                            String paramName = _inputField.getRequestParamName();
                            if(StringUtil.isInvalid(paramName)){
                                paramName = _inputField.getAlias();
                            }
                            if(StringUtil.isInvalid(paramName)){
                                paramName = _inputField.getFieldName();
                            }
                            String paramValue=input.getRequest().getParameter(paramName);
                            if(paramValue==null){//空字符串需要更新,如果为空，则没有传这个参数,不更新
                                continue;
                            }
                        }
                        field.setFieldValue(_inputField.fieldValueToString());
                        updateFieldsList.add(field);
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        if (CollectionUtil.isInvalid(updateFieldsList)) {
            if(StringUtil.isValid(noFieldSkip)){
                return;
            }else {
                throw new LegoException("没有需要更新的字段", ERROR_LEGO_UPDATE_NO_FIELDS);
            }
        }

        try {
            dataManager.update(oi, updateFieldsList, LegoUtil.buildCondition(input));
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_UPDATE);
        }

        //如果output中配置了表单字段，则把Update字段全部写入到output中去
        List<OutputField> outputFieldList = output.getOutputField(IOFT.Update);
        if (CollectionUtil.isValid(outputFieldList)) {
            for (OutputField outputField : outputFieldList) {
                Object value = input.getInputObjectValue(outputField.getFieldName());
                output.setOutputValue(outputField.getFieldName(), value);
            }
        }

        //添加日志
        LogUtil.saveLog(tyDriver, input, output, oi, "update");

    }

    @Override
    public void generate(int apiLegoId, String oiAlias) throws LegoException {
        try {
            List<Field> fields = tyDriver.getTyCacheService().getFields(oiAlias);
            //添加inputfields
            for(Field field:fields){
                if(field.isPk()){
                    InputField inputfield = new InputField();
                    inputfield.setIft(IOFT.Condition.getType());
                    inputfield.setName(field.getName());
                    inputfield.setFieldName(field.getFieldName());
                    inputfield.setAlias(field.getFieldName());
                    inputfield.setApiLegoId(apiLegoId);
                    inputfield.setDt(DT.String.getType());
                    inputfield.setFvt(FVT.Request.getType());
                    inputfield.setRequestParamName(field.getFieldName());
                    inputfield.setDescription(TIP);
                    inputfield.setCt(CT.EQ.getName());
                    tyDriver.getInputFieldManager().add(inputfield);
                    continue;
                }
                InputField inputfield = new InputField();
                inputfield.setIft(IOFT.Update.getType());
                inputfield.setName(field.getName());
                inputfield.setFieldName(field.getFieldName());
                inputfield.setAlias(field.getFieldName());
                inputfield.setApiLegoId(apiLegoId);
                inputfield.setDt(DT.String.getType());
                inputfield.setFvt(FVT.Request.getType());
                inputfield.setRequestParamName(field.getFieldName());
                inputfield.setDescription(TIP);
                tyDriver.getInputFieldManager().add(inputfield);
            }

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_GENERATE);
        }


    }


}
