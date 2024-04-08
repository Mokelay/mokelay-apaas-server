package com.greatbee.core.lego.basic;

import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.*;
import com.greatbee.core.bean.oi.Field;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.lego.*;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取数据
 * <p/>
 * 输入：
 * 1. 需要读取的字段列表(多个ioft= read)
 * 2. 需要读取的条件(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * 输出：
 * 1. 唯一数据对象 OUTPUT_KEY_DATA
 * 2. 针对IOFT = Read的字段的值
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("read")
public class Read extends BaseRead implements Lego,LegoGenerator, ExceptionCode {
    private static final String OUTPUT_KEY_DATA = "data";

    private static final Logger logger = Logger.getLogger(Read.class);

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        ConnectorTree root = buildConnectorTree(tyDriver, input);

        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        try {
            Data data = dataManager.read(root);
            output.setOutputValue(OUTPUT_KEY_DATA, data);

            //读取Read的字段，写入到Output中
            java.util.List<OutputField> outputFields = output.getOutputField(IOFT.Read);
            if (CollectionUtil.isValid(outputFields)) {
                for (OutputField outputField : outputFields) {
                    Object outputFieldValue = data.get(outputField.getFieldName());
                    if(outputFieldValue!=null){//解决如果是 outputField.getAlias() 是data，就会返回null
                        outputField.setFieldValue(outputFieldValue);
                    }
                }
            }

        } catch (DBException e) {
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

    }

    @Override
    public void generate(int apiLegoId, String oiAlias) throws LegoException {
        try {
            java.util.List<Field> fields = tyDriver.getTyCacheService().getFields(oiAlias);
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
                inputfield.setIft(IOFT.Read.getType());
                inputfield.setName(field.getName());
                inputfield.setFieldName(field.getFieldName());
                inputfield.setAlias(field.getFieldName());
                inputfield.setApiLegoId(apiLegoId);
                inputfield.setDt(DT.String.getType());
                inputfield.setDescription(TIP);
                tyDriver.getInputFieldManager().add(inputfield);
            }
            //添加outputFields
            OutputField of = new OutputField();
            of.setAlias(OUTPUT_KEY_DATA);
            of.setDescription(TIP);
            of.setApiLegoId(apiLegoId);
            of.setName(OUTPUT_KEY_DATA);
            of.setFieldName(OUTPUT_KEY_DATA);
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
