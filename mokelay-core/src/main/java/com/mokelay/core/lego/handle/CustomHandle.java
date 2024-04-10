package com.mokelay.core.lego.handle;

import com.mokelay.base.util.ArrayUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.FieldHandle;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.api.util.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 自定义数据处理
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
@Component("customHandle")
public class CustomHandle implements FieldHandle {
    @Override
    public void handle(Output output, OutputField outputField, String[] params) throws LegoException {
        if (ArrayUtil.isValid(params)) {
            for(int i=0;i<params.length;i++){
                String beanId = DataUtil.getString(params[0], null);
                if(StringUtil.isValid(beanId)){
                    ApplicationContext ac = SpringContextUtil.getApplicationContext();
                    FieldHandle fieldHandle = (FieldHandle) ac.getBean(beanId);
                    fieldHandle.handle(output, outputField, params);
                }
            }
        }
    }
}
