package com.mokelay.core.lego.basic;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.base.bean.DBException;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.core.lego.*;
import com.mokelay.core.lego.util.LegoUtil;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.db.database.DataManager;
import com.mokelay.api.util.SpringContextUtil;
import com.mokelay.db.bean.constant.DBMT;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 通过主键字段来做删除操作删除
 * <p/>
 * 输入：
 * 1. 需要删除的条件(多个,ioft= condition)
 * 输出：
 * 1. 删除对象的主键的值(Output_Key_UniqueValue)
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("delete")
public class Delete implements Lego,LegoGenerator,ExceptionCode {

    private static final Logger logger = Logger.getLogger(Delete.class);

//    private static final String Output_Key_UniqueValue = "unique_value";

    @Autowired
    private TYDriver tyDriver;

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
        String dst = ds.getDst();
        DataManager dataManager = (DataManager) wac.getBean(DBMT.Mysql.getType());

        try {
            dataManager.delete(oi, LegoUtil.buildCondition(input));
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_UPDATE);
        }

        //添加日志
        //LogUtil.saveLog(tyDriver, input, output, oi, "delete");

    }

    @Override
    public void generate(int apiLegoId, String oiAlias) throws LegoException {
        /*
        2024-04-09 废弃数据库存储
        TODO 后续升级为JSON存储 ,见ty_model_to_api_and_page的实现方式

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
                    inputfield.setCt(CT.EQ.getName());
                    inputfield.setDt(DT.String.getType());
                    inputfield.setFvt(FVT.Request.getType());
                    inputfield.setRequestParamName(field.getFieldName());
                    inputfield.setDescription(TIP);
                    tyDriver.getInputFieldManager().add(inputfield);
                    break;
                }
            }
            //不需要提那家outputField
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e, ERROR_LEGO_GENERATE);
        }
         */
    }

}
