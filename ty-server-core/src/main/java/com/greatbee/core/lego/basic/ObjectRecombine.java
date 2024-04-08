package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.Data;
import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

/**
 * 对象重组， 将单个值或者单个数组转换成 object或者list    主要和对象分离组件结合使用
 * <p>
 * 输入：
 * 1. Input_Key_Data_Type 组装类型，data：单个对象  list：数组
 * 输出：
 * 1. 最终输出的对象或者list  Output_Key_Data
 * Author :xiaobc
 * Date:17/7/20
 */
@Component("objectRecombine")
public class ObjectRecombine implements Lego, ExceptionCode {

    private static final String Input_Key_Data_Type = "dataType";// 转换数据类型 可以转换data对象或者list数组    data/list
    private static final String Output_Key_Data = "data";

    @Override
    public void execute(Input input, Output output) throws LegoException {

        List<InputField> ifs = input.getInputField(IOFT.Memory);
        //数据类型
        String dataType = input.getInputValue(Input_Key_Data_Type);
        if (StringUtil.isInvalid(dataType)) {
            dataType = "data";//默认转换成对象
        }
        if (StringUtil.equals("data", dataType)) {
            //如果是对象
            Data data = new Data();
            for (InputField _if : ifs) {
                data.put(_if.getFieldName(), _if.getFieldValue());
            }
            output.setOutputValue(Output_Key_Data, data);
        } else if (StringUtil.equals("list", dataType)) {
            //如果是list
            List<Map<String, Object>> list = new ArrayList<>();
            int len = 0;
            for (InputField _if : ifs) {
                Object param = _if.getFieldValue();
                //找到第一个list对象，并且返回他的长度
                if (param instanceof List && ((List) param).size() > 0) {
                    len = ((List) param).size();
                    break;
                }
            }
            for (int i = 0; i < len; i++) {
                Map<String, Object> map = new HashMap<>();
                for (InputField _if : ifs) {
                    Object param = _if.getFieldValue();
                    String ifName = _if.getFieldName();
                    if (param instanceof List) {
                        map.put(ifName,((List) param).get(i));
                    } else {
                        map.put(ifName, param);
                    }
                }
                list.add(map);
            }
            output.setOutputValue(Output_Key_Data, list);
        } else {
            throw new LegoException("转换格式只支持map和list", 300018);
        }
    }
}
