package com.mokelay.core.lego.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.core.bean.constant.FormatType;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * 读取Session的Attribute
 * <p>
 * * 输入：
 * 1. 需要读取的sessionKEY  只能配置一个inputs
 * 2. 读取数据需要转化的格式formater_type，默认是JSON
 * 输出：
 * 1. 翻页数据 OUTPUT_KEY_DATA
 * 2. 如果输出数据是JSON，则输出各个字段的列表(IOFT=common)
 * Author :CarlChen
 * Date:17/8/2
 */
@Component("sessionRead")
public class SessionRead implements Lego {
    private static Logger logger = Logger.getLogger(SessionRead.class);
    private static final String OUTPUT_KEY_DATA = "data";

    //格式类型，json 和 object 两种类型(json/object)
    private static final String INPUT_KEY_FORMATER_TYPE="formater_type";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        java.util.List<InputField> readFields = input.getInputFields();
        //是否json  默认json
        InputField formatTypeField = input.getInputField(INPUT_KEY_FORMATER_TYPE);
        String formatType =(formatTypeField==null||formatTypeField.getFieldValue()==null)?FormatType.JSON.getType():formatTypeField.fieldValueToString();
        boolean isJson = FormatType.JSON.getType().equalsIgnoreCase(formatType);

        Object data = null;
        HttpSession session = input.getRequest().getSession(false);
        if (session != null) {
            if (CollectionUtil.isValid(readFields)) {
                InputField field = null;
                for(InputField readField:readFields){
                    if(!INPUT_KEY_FORMATER_TYPE.equalsIgnoreCase(readField.getFieldName())){
                        field = readField;
                        break;
                    }
                }
                if(field==null){
                    data=null;
                }
                Object obj = session.getAttribute(field.getFieldName());
                if(obj instanceof String){
                    String objStr = DataUtil.getString(obj, null);
                    if (objStr != null) {
                        logger.info("[SessionRead] obj=" + DataUtil.getString(obj, "getString error"));
                        if (isJson) {
                            data = JSON.parseObject(objStr);
                        } else {
                            data = obj;
                        }
                    }
                }else {
                    data = obj;
                }
            }
        }
        output.setOutputValue(OUTPUT_KEY_DATA, data);

        if (data != null&&(data instanceof JSONObject)) {//json格式才会返回，非json不返回
            //读取Read的字段，写入到Output中
            java.util.List<OutputField> outputFields = output.getOutputField(IOFT.Common);
            if (CollectionUtil.isValid(outputFields)) {
                for (OutputField outputField : outputFields) {
                    if (((JSONObject)data).containsKey(outputField.getFieldName())) {
                        Object outputFieldValue = ((JSONObject)data).get(outputField.getFieldName());
                        if (outputFieldValue != null) {//解决如果是 outputField.getAlias() 是data，就会返回null
                            outputField.setFieldValue(outputFieldValue);
                        }
                    }
                }
            }
        }

    }
}
