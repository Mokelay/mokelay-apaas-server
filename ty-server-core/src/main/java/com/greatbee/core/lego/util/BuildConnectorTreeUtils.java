package com.greatbee.core.lego.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.*;
import com.greatbee.core.bean.oi.Connector;
import com.greatbee.core.bean.oi.Field;
import com.greatbee.core.bean.oi.FunctionField;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.view.*;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.manager.TYDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BuildConnectorTreeUtils
 *
 * @author xiaobc
 * @date 17/10/11
 */
public class BuildConnectorTreeUtils implements ExceptionCode {

    private static final Long Lego_Error_Connector_Is_Null = 100000L;

    public  static final String Input_Key_Order_Field = "order_field";
    public static final String Input_Key_Order = "order";
    public static final String Input_Key_Group_Field = "group_field";

    private static Map<String, OIView> oicache = null;

    /**
     * 根据input 构建connectorTree
     *
     * @param tyDriver
     * @param input
     * @throws LegoException
     */
    public static ConnectorTree buildConnectorTree(TYDriver tyDriver, Input input) throws LegoException {
        return buildConnectorTree(tyDriver,input,false);
    }
    /**
     * 根据input 构建connectorTree
     *
     * @param tyDriver
     * @param input
     * @throws LegoException
     */
    public static ConnectorTree buildConnectorTree(TYDriver tyDriver, Input input,Boolean noRead) throws LegoException {
        if(oicache==null){
            oicache = new HashMap<String, OIView>();
        }
        ConnectorTree root = new ConnectorTree();

        String rootOIAlias = input.getApiLego().getOiAlias();
        OIView rootOIView;
        try {
            if (StringUtil.isInvalid(rootOIAlias)) {
                throw new LegoException("没有配置OI", ERROR_LEGO_LIST_NO_OI);
            }
            rootOIView = _readOIViewFromCache(tyDriver, oicache, rootOIAlias);
            root.setOi(rootOIView.getOi());
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
        if(!noRead){
            buildReadFields(tyDriver, input, root, oicache);
        }
        buildConditionFields(tyDriver, input, root, oicache);
        buildGroupFields(tyDriver, input, root, oicache);
        buildOrderFields(tyDriver, input, root, oicache);

        return root;
    }

    /**
     * 从ConnectorPath中获取ConnectorTree
     *
     * @param root          root
     * @param connectorPath 连接路径
     * @param oicache       oicache
     * @return ConnectorTree
     * @throws LegoException
     */
    private static ConnectorTree _getTargetConnectorTree(TYDriver tyDriver, ConnectorTree root, String connectorPath, Map<String, OIView> oicache) throws LegoException {
        if (StringUtil.isValid(connectorPath)) {
            ConnectorTree target = root;
            JSONArray connectorList = JSON.parseArray(connectorPath);
            for (Object aConnectorList : connectorList) {
                JSONObject connectorJSON = (JSONObject) aConnectorList;
                String connectorAlias = connectorJSON.getString("alias");
                try {
                    Connector connector = tyDriver.getTyCacheService().getConnectorByAlias(connectorAlias);
                    //解决连接器被删除后报错不明显问题
                    if(StringUtil.isValid(connectorAlias)&&connector==null){
                        throw new LegoException(connectorAlias+"连接器被删除",Lego_Error_Connector_Is_Null);
                    }
                    OIView oiView = _readOIViewFromCache(tyDriver, oicache, connector.getToOIAlias());
                    target = target.getChildConnectorTree(connector, oiView.getOi());
                } catch (DBException e) {
                    e.printStackTrace();
                    throw new LegoException(e.getMessage(), e, e.getCode());
                }
            }
            return target;
        } else {
            return root;
        }
    }

    /**
     * 从Cache中获取OIView
     *
     * @param oicache oicache
     * @param oiAlias OI别名
     * @return OIView
     * @throws DBException
     */
    private static OIView _readOIViewFromCache(TYDriver tyDriver, Map<String, OIView> oicache, String oiAlias) throws DBException {
        OIView oiView = oicache.get(oiAlias);
        if (oiView == null) {
            oiView = new OIView();

            oiView.setOi(tyDriver.getTyCacheService().getOIByAlias(oiAlias));
            oiView.setFields(tyDriver.getTyCacheService().getFields(oiAlias));

            oicache.put(oiAlias, oiView);
        }
        return oiView;
    }

    /**
     * connectorTree构建read字段
     *
     * @param tyDriver
     * @param input
     * @param root
     * @param oicache
     * @throws LegoException
     */
    private static void buildReadFields(TYDriver tyDriver, Input input, ConnectorTree root, Map<String, OIView> oicache) throws LegoException {
        //需要读取的字段列表
        List<InputField> fields = input.getInputField(IOFT.Read);
        if (CollectionUtil.isValid(fields)) {
            for (InputField f : fields) {
                ConnectorTree target = _getTargetConnectorTree(tyDriver, root, f.getConnectorPath(), oicache);
                try {
                    OIView targetOIView = _readOIViewFromCache(tyDriver, oicache, target.getOi().getAlias());
                    List<Field> oiFields = targetOIView.getFields();
                    Field targetField = null;
                    for (Field field : oiFields) {
                        if (field.getFieldName().equalsIgnoreCase(f.getFieldName())) {
                            targetField = field;
                        }
                    }
                    if(targetField==null){
                        //FunctionField
                        String fieldName = f.getFieldName();
                        // method-params     eg: count-id
                        if(StringUtil.isValid(fieldName)&&fieldName.contains("-")){
                            String[] fun = fieldName.split("-");
                            FunctionField ff = new FunctionField();
                            ff.setFn(fun[0]);
                            ff.setParams(fun[1]);
                            targetField = ff;
                        }
                    }
                    target.addField(f.getAlias(), targetField);
                } catch (DBException e) {
                    e.printStackTrace();
                    throw new LegoException(e.getMessage(), e, e.getCode());
                }
            }
        } else {
            throw new LegoException("没有可以读取的字段", ERROR_LEGO_READ_NO_FIELD);
        }
    }

    /**
     * connectorTree构建condition
     *
     * @param tyDriver
     * @param input
     * @param root
     * @param oicache
     * @throws LegoException
     */
    private static void buildConditionFields(TYDriver tyDriver, Input input, ConnectorTree root, Map<String, OIView> oicache) throws LegoException {
        //搜索的字段列表
        List<InputField> conditionFields = input.getInputField(IOFT.Condition);
        if (CollectionUtil.isValid(conditionFields)) {
            OIView oiView = oicache.get(root.getOi().getAlias());//获取oiView
            List<Field> oiFields = oiView.getFields();
            for (InputField inputField : conditionFields) {
                // 从oiCache中根据inputField获取对应的OI_Field
                Field oiField = null;
                for(Field f :oiFields){
                    if(inputField.getFieldName().equalsIgnoreCase(f.getFieldName())){
                        oiField = f;
                        break;
                    }
                }
                String ct = inputField.getCt();
                if (StringUtil.isInvalid(ct)) {
                    ct = CT.EQ.getName();
                }
                String value = inputField.fieldValueToString();
                if (StringUtil.isValid(value) ||
                        (StringUtil.isInvalid(value) && (CT.NULL.getName().equals(ct) || CT.NotNull.getName().equals(ct)))) {
                    Condition c=null;
                    if(ct.equalsIgnoreCase(CT.Multi.getName())){
                        //如果是混合类型
                        String valueStr = inputField.fieldValueToString();
                        c = buildMultiCondition(valueStr,inputField.getFieldName());
                        // 应该从OI_Field获取DT来设置
                        c.setDt(oiField!=null?oiField.getDt(): DT.String.getType());
                    }else if(ct.equalsIgnoreCase(CT.Custom.getName())){
                        //如果是自定义类型
                        String valueStr = inputField.fieldValueToString();
                        c = buildCustomCondition(valueStr,inputField.getFieldName());
                        // 应该从OI_Field获取DT来设置
                        c.setDt(oiField!=null?oiField.getDt(): DT.String.getType());
                    }else{
                        c = new Condition();
                        c.setConditionFieldName(inputField.getFieldName());
                        c.setConditionFieldValue(inputField.fieldValueToString());
                        c.setCt(ct);
                        // 应该从OI_Field获取DT来设置
                        c.setDt(oiField!=null?oiField.getDt(): DT.String.getType());
                    }
                    ConnectorTree target = _getTargetConnectorTree(tyDriver, root, inputField.getConnectorPath(), oicache);
                    target.addCondition(c);
                }else if(CT.IN.getName().equals(ct)||CT.NOTIN.getName().equals(ct)){
                    //主要解决，in关键字参数为null的时候 应该查询不到数据，后面拼SQL的时候会加上一个不存在的数
                    Condition c = new Condition();
                    c.setConditionFieldName(inputField.getFieldName());
                    c.setConditionFieldValue(inputField.fieldValueToString());
                    c.setCt(ct);
                    // 应该从OI_Field获取DT来设置
                    c.setDt(oiField!=null?oiField.getDt(): DT.String.getType());
                    ConnectorTree target = _getTargetConnectorTree(tyDriver, root, inputField.getConnectorPath(), oicache);
                    target.addCondition(c);
                }
            }
        }
    }

    /**
     * 自定义条件解析
     * @param valueStr
     * @param fieldName
     * @return
     */
    private static Condition buildCustomCondition(String valueStr,String fieldName) throws LegoException {
        if(StringUtil.isInvalid(valueStr)){
            return null;
        }

        boolean jsonConfig = false;
        JSONObject rules = null;
        try{
            rules = JSONObject.parseObject(valueStr);
            if(rules!=null){
                jsonConfig = true;
            }
        }catch (JSONException e){
            jsonConfig = false;
        }

        if(!jsonConfig){
            Condition con = new Condition();
            con.setConditionFieldName(fieldName);
            String []conVals = valueStr.split("_");
            if(StringUtil.isInvalid(CT.getSqlType(conVals[0]))){
                throw new LegoException("自定义条件参数无效",ERROR_LEGO_CUSTOM_CONDITION_PARAM);
            }
            con.setCt(conVals[0]);
            if(conVals.length>1){
                con.setConditionFieldValue(conVals[1]);
            }
            return con;
        }else{
            MultiCondition mc = new MultiCondition();
            mc.setConditionFieldName(fieldName);

            String cg = rules.getString("cg");
            mc.setCg(CG.getCGByName(cg));

            JSONArray conditions = rules.getJSONArray("conditions");
            for (Object condition : conditions) {
                JSONObject conditionConfig = (JSONObject) condition;
                String _cg = conditionConfig.getString("cg");

                //判断是否是MultiConfig
                if (StringUtil.isValid(_cg)) {
                    //仍然是MultiConditions
                    mc.addCondition(buildCustomCondition(conditionConfig.toJSONString(),fieldName));
                } else {
                    Condition finalCondition = new Condition();
                    finalCondition.setConditionFieldName(fieldName);
                    finalCondition.setCt(conditionConfig.getString("ct"));
                    finalCondition.setConditionFieldValue(conditionConfig.getString("conditionFieldValue"));
                    mc.addCondition(finalCondition);
                }
            }
            return mc;
        }
    }

    /**
     * 例如： =1||IS NULL
     * 混合条件类型解析 value值
     * @param valueStr
     */
    public static Condition buildMultiCondition(String valueStr,String fieldName){
        if(StringUtil.isInvalid(valueStr)){
            return null;
        }
        MultiCondition orC = new MultiCondition();
        if(valueStr.contains("||")){
            orC.setCg(CG.OR);
        }
        String[] orValueArray = valueStr.split("\\|\\|");
        for(int i=0;i<orValueArray.length;i++){
            String orValue = orValueArray[i];

            if(orValue.contains("&")){
                MultiCondition andC=new MultiCondition();;
                String[] andValueArray = orValue.split("&");
                if(andValueArray.length<=0){
                    continue;
                }
                andC.setCg(CG.AND);
                for(int j=0;j<andValueArray.length;j++){
                    String andValue = andValueArray[j];
                    Condition c = buildSingleCondition(fieldName,andValue);
                    andC.addCondition(c);
                }
                orC.addCondition(andC);
            }else{
                Condition c = buildSingleCondition(fieldName,orValue);
                orC.addCondition(c);
            }
        }
        return orC;
    }

    /**
     * 混合条件  解析条件value 构建 单个Condition
     * @param fieldName
     * @param multiString
     * @return
     */
    private static Condition buildSingleCondition(String fieldName,String multiString){
        if(StringUtil.isInvalid(multiString)){
            return null;
        }
        Condition c = new Condition();
        for(CT ct:CT.values()){
            if(CT.Multi.getName().equalsIgnoreCase(ct.getName())){
               continue;
            }
            // 因为不确定混合条件的参数 所以这里只能用contains  比如  neq'' 不等于空串
            if(multiString.contains(ct.getName())){
                c.setConditionFieldName(fieldName);
                if(CT.isNeedFieldValue(ct.getName())){
                    c.setConditionFieldValue(multiString.replace(ct.getName(), ""));
                }
                c.setCt(ct.getName());
                break;
            }
        }
        return c;
    }

    /**
     * connectorTree构建group
     *  群组目前只支持单表
     * @param tyDriver
     * @param input
     * @param root
     * @param oicache
     * @throws LegoException
     */
    private static void buildGroupFields(TYDriver tyDriver, Input input, ConnectorTree root, Map<String, OIView> oicache) throws LegoException {
        //获取GroupBy
        InputField groupByField = input.getInputField(Input_Key_Group_Field);
        if (groupByField != null && StringUtil.isValid(groupByField.fieldValueToString())) {
            GroupBy groupBy = new GroupBy();
            String groupFieldValue = groupByField.fieldValueToString();
            groupBy.setGroupFieldName(groupFieldValue);
            StringBuilder connectorPath =  new StringBuilder();
            if(groupFieldValue.contains(".")){
                String[] groupFieldValueArray = groupFieldValue.split("\\.");
                groupBy.setGroupFieldName(groupFieldValueArray[groupFieldValueArray.length-1]);
                connectorPath.append("[");
                for(int i=0;i<groupFieldValueArray.length-1;i++){
                    if(i!=0){
                        connectorPath.append(",");
                    }
                    connectorPath.append("{alias:\""+groupFieldValueArray[i]+"\"}");
                }
                connectorPath.append("]");
            }else{
                connectorPath.append(groupByField.getConnectorPath()==null?"":groupByField.getConnectorPath());
            }

            ConnectorTree target = _getTargetConnectorTree(tyDriver, root, ("".equalsIgnoreCase(connectorPath.toString())?null:connectorPath.toString()), oicache);
            target.setGroupBy(groupBy);
        }
    }

    /**
     * connectorTree构建Order
     *
     * @param tyDriver
     * @param input
     * @param root
     * @param oicache
     * @throws LegoException
     */
    private static void buildOrderFields(TYDriver tyDriver, Input input, ConnectorTree root, Map<String, OIView> oicache) throws LegoException {
        //获取Order
        InputField orderField = input.getInputField(Input_Key_Order_Field);
        String orderType = input.getInputValue(Input_Key_Order);
        if (orderField != null && StringUtil.isValid(orderField.fieldValueToString())) {

            String orderFields = orderField.fieldValueToString();
            if(orderFields.contains(",")){
                //如果有多个
                String[] orderFieldString = orderFields.split(",");
                String[] orderTypeString = orderType.split(",");
                MultiOrderBy mob = new MultiOrderBy();
                for(int i=0;i<orderFieldString.length;i++){
                    String orderString = orderFieldString[i];
                    String orderByType = orderTypeString[i];
                    OrderBy orderBy = new OrderBy();
                    orderBy.setOrderFieldName(orderString);
                    if (StringUtil.isValid(orderByType)) {
                        orderBy.setOrder(Order.getOrder(orderByType));
                    }
                    mob.addOrderBy(orderBy);
                }
                ConnectorTree target = _getTargetConnectorTree(tyDriver, root, orderField.getConnectorPath(), oicache);
                target.setOrderBy(mob);
            }else{
                OrderBy orderBy = new OrderBy();
                String orderFieldValue = orderField.fieldValueToString();
                orderBy.setOrderFieldName(orderFieldValue);
                StringBuilder connectorPath =  new StringBuilder();
                if(orderFieldValue.contains(".")){
                    String[] orderFieldValueArray = orderFieldValue.split("\\.");
                    orderBy.setOrderFieldName(orderFieldValueArray[orderFieldValueArray.length - 1]);
                    connectorPath.append("[");
                    for(int i=0;i<orderFieldValueArray.length-1;i++){
                        if(i!=0){
                            connectorPath.append(",");
                        }
                        connectorPath.append("{alias:\""+orderFieldValueArray[i]+"\"}");
                    }
                    connectorPath.append("]");
                }else{
                    connectorPath.append(orderField.getConnectorPath()==null?"":orderField.getConnectorPath());
                }
                if (StringUtil.isValid(orderType)) {
                    orderBy.setOrder(Order.getOrder(orderType));
                }
                ConnectorTree target = _getTargetConnectorTree(tyDriver, root, ("".equalsIgnoreCase(connectorPath.toString())?null:connectorPath.toString()), oicache);
                target.setOrderBy(orderBy);
            }
        }

    }

    /**
     * 清空oiCache缓存
     */
    public static void clearOICache(){
        if(oicache!=null){
            oicache = null;
        }
    }

}
