package com.mokelay.core.manager.utils;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.db.bean.constant.Order;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.APILego;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.core.lego.util.BuildConnectorTreeUtils;
import com.mokelay.core.lego.util.LegoUtil;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.db.database.mysql.manager.MysqlDataManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CustomBuildUtils
 * 需要用到BaseRead 中的buildConnectorTree 方法
 *
 * @author xiaobc
 * @date 17/10/11
 */
public class CustomBuildUtils implements ExceptionCode {

    private static final Logger logger = Logger.getLogger(CustomBuildUtils.class);

    /**
     * 构建自定义查询sql
     *
     * @param tyDriver
     * @param oiAlias
     * @param reads      (ift,connectorPath,fieldName,alias)
     * @param conditions (ct,connectorPath,fieldName,fieldValue)
     * @return
     * @throws DBException
     * @throws LegoException
     */
    public static ConnectorTree customBuildSelectConnectorTree(TYDriver tyDriver, String oiAlias, List reads, List<InputField> conditions) throws LegoException {
        return customBuildSelectConnectorTree(tyDriver, oiAlias, reads, conditions, null, false);
    }

    /**
     * 构建自定义查询sql
     *
     * @param tyDriver
     * @param oiAlias
     * @param reads      (ift,connectorPath,fieldName,alias)
     * @param conditions (ct,connectorPath,fieldName,fieldValue)
     * @return
     * @throws DBException
     * @throws LegoException
     */
    public static ConnectorTree customBuildSelectConnectorTree(TYDriver tyDriver, String oiAlias, List reads, List<InputField> conditions, InputField orderField, boolean isASC) throws LegoException {
        String orderBy = Order.DESC.getType();
        if (isASC) {
            orderBy = Order.ASC.getType();
        }
        return customBuildSelectConnectorTree(tyDriver, oiAlias, reads, conditions, orderField, orderBy);
    }


    /**
     * 构建自定义查询sql  多字段排序
     *
     * @param tyDriver
     * @param oiAlias
     * @param reads      (ift,connectorPath,fieldName,alias)
     * @param conditions (ct,connectorPath,fieldName,fieldValue)
     * @return
     * @throws DBException
     * @throws LegoException
     */
    public static ConnectorTree customBuildSelectConnectorTree(TYDriver tyDriver, String oiAlias, List reads, List<InputField> conditions, InputField orderField, String orderBy) throws LegoException {
        Input input = new Input();
        APILego al = new APILego();
        al.setOiAlias(oiAlias);
        input.setApiLego(al);

        List<InputField> inputFields = new ArrayList<>();
        if (reads == null) {
            //如果read为空，就查询所有的字段
            List<Field> fields = null;
            try {
                fields = tyDriver.getTyCacheService().getFields(oiAlias);
            } catch (DBException e) {
                e.printStackTrace();
                throw new LegoException(e.getMessage(), e, e.getCode());
            }
            for (Field f : fields) {
                InputField _if = new InputField();
                _if.setFieldName(f.getFieldName());
                _if.setAlias(f.getFieldName());
                _if.setIft(IOFT.Read.getType());
                inputFields.add(_if);
            }
        } else {
            if (reads.get(0) instanceof InputField) {
                inputFields.addAll(reads);
            } else if (reads.get(0) instanceof String) {
                for (int i = 0; i < reads.size(); i++) {
                    String item = (String) reads.get(i);
                    InputField _if = new InputField();
                    _if.setFieldName(item);
                    _if.setAlias(item);
                    _if.setIft(IOFT.Read.getType());
                    inputFields.add(_if);
                }
            } else {
                throw new LegoException("查询参数错误", 300001);
            }
        }
        input.setInputFields(inputFields);

        if (CollectionUtil.isValid(conditions)) {
            //如果条件有效
            inputFields.addAll(conditions);
        }
        if (orderField != null) {
            orderField.setFieldName(BuildConnectorTreeUtils.Input_Key_Order_Field);
            inputFields.add(orderField);
            InputField __if = new InputField();
            __if.setFieldName(BuildConnectorTreeUtils.Input_Key_Order);
            __if.setFieldValue(orderBy);
            inputFields.add(__if);
        }
        return BuildConnectorTreeUtils.buildConnectorTree(tyDriver, input);
    }


    /**
     * 自定义添加和  主键更新方法
     *
     * @param tyDriver
     * @param dataManager
     * @param oiAlias
     * @param params
     * @return
     * @throws LegoException
     */
    public static Object customSave(TYDriver tyDriver, MysqlDataManager dataManager, String oiAlias, Map<String, Object> params) throws LegoException {
        if (params == null) {
            return null;
        }
        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);

            List<Field> fieldsList = new ArrayList<Field>();

            Field pkField = null;//主键字段
            boolean isUpdate = false;

            for (Field field : oiFields) {
                Object target = null;
                //字段
                for (Map.Entry entry : params.entrySet()) {
                    if (field.getFieldName().equalsIgnoreCase((String) entry.getKey())) {
                        target = entry.getValue();
                        break;
                    }
                }
                //根据主键 更新
                if (field.isPk() && target != null) {
                    pkField = (Field) field.clone();
                    pkField.setFieldValue(DataUtil.getString(target, ""));
                    isUpdate = true;
                }

                //针对从上一个节点或者http请求获取的数据填充
                if (target != null) {
                    Field newField = (Field) field.clone();
                    newField.setFieldValue(DataUtil.getString(target, ""));
                    fieldsList.add(newField);
                }
            }
            if (isUpdate) {
                //更新
                if (CollectionUtil.isValid(fieldsList) && pkField != null) {
                    dataManager.update(oi, fieldsList, pkField);
                } else {
                    throw new LegoException("没有需要更新的字段", ERROR_LEGO_UPDATE_NO_FIELDS);
                }
            } else {
                //添加
                if (CollectionUtil.isValid(fieldsList)) {
                    String uniqueValue = dataManager.create(oi, fieldsList);
                    return uniqueValue;
                } else {
                    throw new LegoException("没有需要添加的字段", ERROR_LEGO_ADD_NO_FIELDS);
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        return null;
    }


    /**
     * 自定义条件更新
     *
     * @param tyDriver
     * @param dataManager
     * @param oiAlias
     * @param params
     * @return
     * @throws LegoException
     */
    public static void customConditionUpdate(TYDriver tyDriver, MysqlDataManager dataManager, String oiAlias, Map<String, Object> params, List<InputField> conditions) throws LegoException {
        if (params == null) {
            return;
        }
        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);

            List<Field> fieldsList = new ArrayList<Field>();

            for (Field field : oiFields) {
                Object target = null;
                //字段
                for (Map.Entry entry : params.entrySet()) {
                    if (field.getFieldName().equalsIgnoreCase((String) entry.getKey())) {
                        target = entry.getValue();
                        break;
                    }
                }
                //针对从上一个节点或者http请求获取的数据填充
                if (target != null) {
                    Field newField = (Field) field.clone();
                    newField.setFieldValue(DataUtil.getString(target, ""));
                    fieldsList.add(newField);
                }
            }
            Input input = new Input();
            APILego al = new APILego();
            al.setOiAlias(oiAlias);
            input.setApiLego(al);
            //添加 IOFT 为Condition 的inputField
            input.setInputFields(conditions);

            //更新
            if (CollectionUtil.isValid(fieldsList) && input != null) {
                dataManager.update(oi, fieldsList, LegoUtil.buildCondition(input));
            } else {
                throw new LegoException("没有需要更新的字段", ERROR_LEGO_UPDATE_NO_FIELDS);
            }
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }

    /**
     * 自定义条件删除
     *
     * @param tyDriver
     * @param dataManager
     * @param oiAlias
     * @return
     * @throws LegoException
     */
    public static void customConditionDelete(TYDriver tyDriver, MysqlDataManager dataManager, String oiAlias, List<InputField> conditions) throws LegoException {

        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            Input input = new Input();
            APILego al = new APILego();
            al.setOiAlias(oiAlias);
            input.setApiLego(al);
            //添加 IOFT 为Condition 的inputField
            input.setInputFields(conditions);
            dataManager.delete(oi, LegoUtil.buildCondition(input));

        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }


}
