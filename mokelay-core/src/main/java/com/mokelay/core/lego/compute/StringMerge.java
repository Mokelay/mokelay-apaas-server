package com.mokelay.core.lego.compute;

import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串合并
 * <p/>
 * 输入：
 * 1.
 * 输出：
 * 1. 合并后的字符串
 * <p/>
 * Author: CarlChen
 */
@Component("stringMerge")
public class StringMerge implements Lego, ExceptionCode {
    private static final String Output_Key_String_Merge = "string_merge";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        List<String> strList = new ArrayList<String>();

        List<InputField> inputFields = input.getInputFields();
        if (CollectionUtil.isValid(inputFields)) {
            for (InputField inputField : inputFields) {
                Object value = inputField.getFieldValue();
                if (value != null) {
                    if (value instanceof List) {
                        if (CollectionUtil.isValid((List) value)) {
                            for (Object o : (List) value) {
                                if (o != null) {
                                    strList.add(o.toString());
                                }
                            }
                        }
                    } else {
                        strList.add(value.toString());
                    }
                }
            }
        }

        if (CollectionUtil.isValid(strList)) {
            output.setOutputValue(Output_Key_String_Merge, StringUtil.toString(strList.toArray(), ','));
        }
    }
}
