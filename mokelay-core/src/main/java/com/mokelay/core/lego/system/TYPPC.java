package com.mokelay.core.lego.system;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * TYPPC
 *
 * @author xiaobc
 * @date 17/9/11
 */
public class TYPPC extends PropertyPlaceholderConfigurer {

    private static Map<String, String> TY_Prop_Data;

//    @Override
//    public void setLocation(Resource location) {
//        if(StringUtil.isValid(Application.configPath)){
//            System.out.println("configPath========================================"+ Application.configPath);
//            FileSystemResource resource=new FileSystemResource(Application.configPath);
//            super.setLocation(resource);
//        }else{
//            System.out.println("configPath======================================== default config");
//            super.setLocation(location);
//        }
//    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        TY_Prop_Data = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = String.valueOf(props.get(keyStr));
            TY_Prop_Data.put(keyStr, value);
        }
    }

    public static String getTYProp(String name) {
        return TY_Prop_Data.get(name);
    }

    public static final String Upload_Temp_Dir() {
        return TYPPC.getTYProp("upload.temp.dir");
    }
}
