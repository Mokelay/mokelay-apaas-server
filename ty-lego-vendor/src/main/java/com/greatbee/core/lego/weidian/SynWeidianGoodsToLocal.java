package com.greatbee.core.lego.weidian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.CT;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.core.bean.constant.DT;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.oi.Field;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.server.APILego;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.util.SpringContextUtil;
import com.greatbee.core.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * synWeidianGoodsToLocal   同步微店商品  到管理后台
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("synWeidianGoodsToLocal")
public class SynWeidianGoodsToLocal extends BaseRead implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(SynWeidianGoodsToLocal.class);
    @Autowired
    private TYDriver tyDriver;

    private static final String Goods_Id = "id";
    private static final String Weidian_Goods_Url="https://weidian.com/item.html?itemID=";//微店商品跳转地址   后面加上微店商品编号

    //微店的 id 和 密钥
    private static final String Input_Key_Weidian_AppId = "appId";
    private static final String Input_Key_Weidian_AppSecret = "appSecret";

    //内存字段组   fieldName 是微店的字段名，alias是 管理后台的字段名。通过内存字段组  将微店返回的字段和后台管理的字段一一对应起来
    private static final String Input_Key_Weidian_Product_Itemid="itemid";//商品编号     唯一值,必填，通过他判断是否数据库中有这条商品,没有就新增,有就更新
    private static final String Input_Key_Weidian_Product_Item_Name="item_name";//商品标题
    private static final String Input_Key_Weidian_Product_Item_Desc="item_desc";//商品描述
    private static final String Input_Key_Weidian_Product_Imgs="imgs";//商品主图  数组结构
    private static final String Input_Key_Weidian_Product_Price="price";//商品价格

    private static final String Input_Key_Weidian_Product_Item_url = "itemUrl";//微店商品详情地址

    //创建字段组  如果有新增同步，有些字段需要手动设置，可以在创建字段里设置

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String appId = input.getInputValue(Input_Key_Weidian_AppId);
        String appSecret = input.getInputValue(Input_Key_Weidian_AppSecret);
        if(StringUtil.isInvalid(appId)||StringUtil.isInvalid(appSecret)){
            throw new LegoException("微店信息无效", VendorExceptionCode.Lego_Error_Weidian_Auth_Error);
        }
        List<InputField> fields = input.getInputField(IOFT.Memory);
        String goodsKey = _getInputField(input, Input_Key_Weidian_Product_Itemid).getAlias();//产品的唯一标识  管理后台的字段名

        APILego apiLego = input.getApiLego();

        //通过 oiAlias获取OI
        OI oi;
        List<Field> oiFields;
        try {
            oi = tyDriver.getTyCacheService().getOIByAlias(apiLego.getOiAlias());
            oiFields = tyDriver.getTyCacheService().getFields(apiLego.getOiAlias());
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }


        //=======查询管理后台产品列表 start =======
        //查询我们后台所有产品列表
        APILego queryApiLego = new APILego();
        queryApiLego.setOiAlias(apiLego.getOiAlias());
        queryApiLego.setLegoAlias("list");
        List<InputField> inputFields = new ArrayList<>();
        InputField idIf = new InputField();
        idIf.setAlias(Goods_Id);
        idIf.setFieldName(goodsKey);//内存字段的alias 就是我们后台的字段名  fileName 是微店返回的字段名
        idIf.setCt(CT.EQ.getName());
        idIf.setIft(IOFT.Read.getType());
        idIf.setDt(DT.String.getType());
        inputFields.add(idIf);

        Input queryInput = new Input(input.getRequest(),input.getResponse());
        queryInput.setApiLego(queryApiLego);
        queryInput.setInputFields(inputFields);
        ConnectorTree root = buildConnectorTree(tyDriver, queryInput);
        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        List<Data> goodsList = null;
        try {
            DataList dataList = dataManager.list(root);
            goodsList = dataList.getList();
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        //=======查询管理后台产品列表   end =======

        //同步商品，从第一页开始每页50条记录
        synProduct(dataManager,oi,oiFields,input,"1","50",goodsList,fields,appId,appSecret);

    }

    /**
     * 同步商品 分页拉取所有数据，然后同步到管理后台
     *
     * 这个方法递归调用 知道最后一页
     */
    private void synProduct(RelationalDataManager dataManager,OI oi,List<Field> oiFields,Input input,String page_num,String page_size,List<Data> goodsList,List<InputField> fields,String appId,String appSecret) throws LegoException {
        logger.info("synProduct page_num : "+page_num +"| page_size : "+page_size);
        String response = WDUtil.weidianGetProductPage(appId, appSecret, page_num, page_size, 1);
        logger.info("weidianGetItems : " + response);
        JSONObject res = JSON.parseObject(response);
        String goodsKey = _getInputField(input, Input_Key_Weidian_Product_Itemid).getAlias();//产品的唯一标识  管理后台的字段名
        boolean synContinue = false;
        if(res.containsKey("status") && res.getJSONObject("status").getInteger("status_code")==0){
            //成功请求
            JSONObject result = res.getJSONObject("result");//获取所有产品详情列表
            //总记录条数
            int totalItems = result.getInteger("total_num");
            //判断是否还需要继续往下同步
            if(Integer.valueOf(page_num) * Integer.valueOf(page_size) < totalItems){
                //当前查询的记录数小于总记录数，则继续向下同步
                synContinue = true;
            }
            //商品列表 一页最大50条
            JSONArray items = result.getJSONArray("items");
            if(items.size()<=0){
                return;
            }

            //循环同步
            for(int i=0;i<items.size();i++){
                JSONObject item = items.getJSONObject(i);
                String itemId = item.getString(Input_Key_Weidian_Product_Itemid);
//                String itemName = item.getString(Input_Key_Weidian_Product_Item_Name);
//                String itemDesc = item.getString(Input_Key_Weidian_Product_Item_Desc);
//                String price = item.getString(Input_Key_Weidian_Product_Price);
                JSONArray imgs = item.getJSONArray(Input_Key_Weidian_Product_Imgs);
                String img = "";
                if(imgs.size()>0){
                    img = imgs.getString(0);
                }
                if(StringUtil.isInvalid(itemId)){
                    continue;
                }
                boolean isCreate = true;//是创建
                if(goodsList!=null && goodsList.size()>0){
                    for(Data data:goodsList){
                        //订单编号
                        String key = data.getString(Goods_Id);
                        if(itemId.equalsIgnoreCase(key)){
                            //有这个产品 更新逻辑
                            isCreate = false;

                            List<Field> updateFieldsList = new ArrayList<>();
                            if (CollectionUtil.isValid(oiFields)) {
                                for (Field field : oiFields) {
                                    InputField target = null;
                                    for (InputField inputField : fields) {
                                        if (inputField.getAlias().equalsIgnoreCase(field.getFieldName())) {//alias 是管理后台的字段名，fieldName是微店返回的字段名
                                            target = inputField;
                                            break;
                                        }
                                    }
                                    if (target != null) {
                                        String val = target.fieldValueToString();
                                        if(StringUtil.isInvalid(val)){
                                            if(target.getFieldName().equalsIgnoreCase(Input_Key_Weidian_Product_Imgs)){
                                                //因为图片 微店返回是数组形式
                                                val = img;//fieldName是微店返回的字段名
                                            }else if(target.getFieldName().equalsIgnoreCase(Input_Key_Weidian_Product_Item_url)){
                                                //这个字段是单独创建的  并不是微店接口返回的
                                                continue;
                                            } else{
                                                val = item.getString(target.getFieldName());//fieldName是微店返回的字段名
                                            }
                                        }else{
                                            //有fieldValue值，可能是添加字段需要的字段  这个字段就不更新
                                            continue;
                                        }
                                        field.setFieldValue(val);
                                        updateFieldsList.add(field);
                                    }
                                }
                            }
                            try {
                                Input conditionInput = new Input(input.getRequest(),input.getResponse());
                                List<InputField> conditionIfs = new ArrayList<>();
                                InputField conditionIf = new InputField();
                                conditionIf.setCt(CT.EQ.getName());
                                conditionIf.setFieldName(goodsKey);
                                conditionIf.setAlias(goodsKey);
                                conditionIf.setFieldValue(itemId);
                                conditionIf.setDt(DT.String.getType());
                                conditionIf.setIft(IOFT.Condition.getType());

                                conditionIfs.add(conditionIf);
                                conditionInput.setInputFields(conditionIfs);

                                dataManager.update(oi, updateFieldsList, LegoUtil.buildCondition(conditionInput));
                            } catch (DBException e) {
                                e.printStackTrace();
                                break;
                            }
                            break;
                        }
                    }
                }
                if(isCreate){
                    //创建产品逻辑：
                    List<Field> createFieldsList = new ArrayList<>();
                    if (CollectionUtil.isValid(oiFields)) {
                        for (Field field : oiFields) {
                            InputField target = null;
                            for (InputField inputField : fields) {
                                if (inputField.getAlias().equalsIgnoreCase(field.getFieldName())) {//alias 是管理后台的字段名，fieldName是微店返回的字段名
                                    target = inputField;
                                    break;
                                }
                            }
                            if (target != null) {
                                String val = target.fieldValueToString();
                                if(StringUtil.isInvalid(val)){
                                    if(target.getFieldName().equalsIgnoreCase(Input_Key_Weidian_Product_Imgs)){
                                        //因为图片 微店返回是数组形式
                                        val = img;//fieldName是微店返回的字段名
                                    }else if(target.getFieldName().equalsIgnoreCase(Input_Key_Weidian_Product_Item_url)){
                                        //单独添加的字段，如果有itemUrl 表示微店商品详情的地址
                                        val = Weidian_Goods_Url+itemId;
                                    }else{
                                        val = item.getString(target.getFieldName());//fieldName是微店返回的字段名
                                    }
                                }
                                field.setFieldValue(val);
                                createFieldsList.add(field);
                            }
                        }
                    }
                    try {
                        String uniqueValue = dataManager.create(oi, createFieldsList);
                    } catch (DBException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }

            if(synContinue){
                //继续向下同步
                synProduct(dataManager,oi,oiFields,input,String.valueOf(Integer.getInteger(page_num)+1),page_size,goodsList,fields,appId,appSecret);
            }
        }

    }


    /**
     * 通过fieldName 来拿inputfield
     * @param input
     * @param name
     * @return
     */
    private InputField _getInputField(Input input,String name){
        List<InputField> inputFields = input.getInputFields();
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                String key = inputField.getFieldName();
                if (key.equalsIgnoreCase(name)) {
                    return inputField;
                }
            }
        }
        return null;
    }

}
