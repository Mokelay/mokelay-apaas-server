package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.CT;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.core.bean.constant.DT;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.APILego;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.basic.ext.BaseRead;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.util.SpringContextUtil;
import com.greatbee.core.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 计算自增长字段的下一个值
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("autoIncrementNextValue")
public class AutoIncrementNextValue extends BaseRead implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(AutoIncrementNextValue.class);

    private static final String Max_Value = "maxValue";

    private static final String Input_Key_Index_Field_Name="indexFieldName";//自增的字段   自增的字段应该是int类型
    private static final String Input_Key_Auto_Increment_Length="length";//自增的字段的长度，超出的部分前面用0代替


    private static final String Output_Key_Next_Value = "nextValue";//返回的 自增的下一个数   比如： 0002

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String result = null;
        int length = DataUtil.getInt(input.getInputValue(Input_Key_Auto_Increment_Length), 6);//默认长度6
        if(length<=0){
            length = 6;
        }
        //自增长的字段名
        String indexFieldName = input.getInputValue(Input_Key_Index_Field_Name);
        if(StringUtil.isInvalid(indexFieldName)){
            throw new LegoException("没有设置自增长字段", VendorExceptionCode.Lego_Error_Auto_Increment_FieldName_Null);
        }
        String maxVal = queryMaxValue(input,indexFieldName);
        logger.info("==============maxValue:"+maxVal);

        if(StringUtil.isInvalid(maxVal)){
            //如果没有数据就从1开始
            maxVal = "0";
        }

        long nextVal = DataUtil.getLong(maxVal,0)+1;
        result = String.valueOf(nextVal);//转换成string

        if(result.length()<length){
            //不足位数的字符串 用0 填充
            int len = length - result.length();
            for(int i=0;i<len;i++){
                result ="0"+result;
            }
        }
        //返回客户端
        output.setOutputValue(Output_Key_Next_Value,result);
    }

    /**
     * 计算字段的最大值  select max(indexFieldName) from xxx;
     * @param input
     * @param indexFieldName
     * @return
     * @throws LegoException
     */
    private String queryMaxValue(Input input,String indexFieldName) throws LegoException {
        APILego apiLego = input.getApiLego();

        //查询我们后台所有产品列表
        APILego queryApiLego = new APILego();
        queryApiLego.setOiAlias(apiLego.getOiAlias());
        queryApiLego.setLegoAlias("read");
        java.util.List<InputField> inputFields = new ArrayList<>();
        InputField idIf = new InputField();
        idIf.setAlias(Max_Value);
        idIf.setFieldName("max-" + indexFieldName);//内存字段的alias 就是我们后台的字段名  fileName 是微店返回的字段名
        idIf.setCt(CT.EQ.getName());
        idIf.setIft(IOFT.Read.getType());
        idIf.setDt(DT.String.getType());
        inputFields.add(idIf);

        Input queryInput = new Input(input.getRequest(),input.getResponse());
        queryInput.setApiLego(queryApiLego);
        queryInput.setInputFields(inputFields);

        ConnectorTree root = buildConnectorTree(tyDriver, queryInput);
        RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
        String result = null;
        try {
            Data data = dataManager.read(root);
            if(data!=null && data.containsKey(Max_Value)){
                result = data.getString(Max_Value);
            }
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        return result;
    }


}
