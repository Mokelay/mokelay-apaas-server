package com.greatbee.core.lego.handle;

import com.greatbee.base.util.ArrayUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.FieldHandle;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.util.SpringContextUtil;
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
