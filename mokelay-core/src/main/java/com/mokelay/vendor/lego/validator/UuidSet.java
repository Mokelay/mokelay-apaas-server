package com.mokelay.vendor.lego.validator;

import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.FieldValidation;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 给输入字段赋予uuid 随机生成数   支持长度参数
 * <p/>
 * Author :xiaobc
 * Date:18/10/25
 */
@Component("uuidSet")
public class UuidSet implements FieldValidation {

    @Override
    public void validate(Input input, InputField inputField, String[] params) throws LegoException {
        //默认的uuid 长度是36位
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        inputField.setFieldValue(uuid);
        if(params.length>0){
            int len = Integer.valueOf(params[0]);
            StringBuilder result = new StringBuilder();
            if(len>uuid.length()){
                //如果要求的长度小于uuid 去除-的长度 后面补0
                int fillLen= len - uuid.length();
                result.append(uuid);
                for(;fillLen>0;fillLen--){
                    result.append("0");
                }
            }else{
                result.append(uuid.substring(0,len));
            }
            inputField.setFieldValue(result.toString());
        }
    }

}
