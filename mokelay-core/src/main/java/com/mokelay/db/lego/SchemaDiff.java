package com.mokelay.db.lego;

import java.util.ArrayList;
import java.util.List;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.bean.view.DiffItem;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.SchemaDataManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("schemaDiff")
public class SchemaDiff implements Lego, ExceptionCode {

    private static final Logger logger = Logger.getLogger(SchemaDiff.class);

    @Autowired
    private SchemaDataManager mysqlDataManager;

    /**
     * 执行
     */
    @Override
    public void execute(Input input, Output output) throws LegoException {

        InputField oiListField = input.getInputField("oi_list");
        InputField fieldListField = input.getInputField("field_list");
        InputField dsField = input.getInputField("ds");

        if (oiListField == null || oiListField.getFieldValue() == null) {
            //没有输入ds的alias
            throw new LegoException("缺少字段 oi_list", ERROR_FIELD_VALIDATE_PARAMS_INVALID);
        }
        if (fieldListField == null || fieldListField.getFieldValue() == null) {
            //没有输入ds的alias
            throw new LegoException("缺少字段 field_list", ERROR_FIELD_VALIDATE_PARAMS_INVALID);
        }
        if (dsField == null || dsField.getFieldValue() == null) {
            //没有输入ds的alias
            throw new LegoException("缺少字段 ds", ERROR_FIELD_VALIDATE_PARAMS_INVALID);
        }

        DataList oiDataList = (DataList) oiListField.getFieldValue();
        DataList fieldDataList = (DataList) fieldListField.getFieldValue();
        List<OIView> oiViewList = new ArrayList<>();
        List<Field> fieldList = new ArrayList<>();
        //组装Field列表
        if (CollectionUtil.isValid(fieldDataList.getList())) {
            for (Object data : fieldDataList.getList()) {
                Data item = (Data) data;
                Field field = new Field();
                field.setId(item.getInt("id"));
                field.setName(item.getString("name"));
                field.setDescription(item.getString("description"));
                field.setFieldName(item.getString("fieldName"));
                field.setOiAlias(item.getString("oiAlias"));
                field.setDt(item.getString("dt"));
                field.setPk(item.getInt("pk") == 1 ? true : false);
                field.setFieldLength(item.getInt("fieldLength"));
                fieldList.add(field);
            }
        }

        //组装OI列表
        if (CollectionUtil.isValid(oiDataList.getList())) {
            for (Object data : oiDataList.getList()) {
                OIView oiView = new OIView();
                Data item = (Data) data;
                OI oi = new OI();
                oi.setDsAlias(item.getString("dsAlias"));
                oi.setName(item.getString("name"));
                oi.setAlias(item.getString("alias"));
                oi.setResource(item.getString("resource"));
                oiView.setOi(oi);
//                oiView.setFields();
                List<Field> tmpFieldList = new ArrayList<>();
                for (Field field : fieldList) {
                    if (field.getOiAlias().equalsIgnoreCase(oi.getAlias())) {
                        tmpFieldList.add(field);
                    }
                }
                oiView.setFields(tmpFieldList);
                oiViewList.add(oiView);
            }
        }
        //组装DS
        Data dsData = (Data) dsField.getFieldValue();
        DS ds = new DS();
        if (dsData != null) {
            ds.setDst(dsData.getString("dst"));
            ds.setConnectionUsername(dsData.getString("connectionUsername"));
            ds.setDsConfigFrom(dsData.getString("dsConfigFrom"));
            ds.setName(dsData.getString("name"));
            ds.setDescription(dsData.getString("description"));
            ds.setAlias(dsData.getString("alias"));
            ds.setConnectionUrl(dsData.getString("connectionUrl"));
            ds.setId(dsData.getInt("id"));
            ds.setConnectionPassword(dsData.getString("connectionPassword"));
        }

        //获取属性的内容
//        DSView diffDSView = (DSView) dsInputField.getFieldValue();
        DSView diffDSView = new DSView();
        diffDSView.setOiViews(oiViewList);
        diffDSView.setDs(ds);

        try {
            //调用接口
            List<DiffItem> diffItemList = mysqlDataManager.diff(diffDSView);
            //设置返回值
            output.setOutputValue("diffItemList", diffItemList);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("调用diff接口异常");
            logger.error(e);
        }
    }

}
