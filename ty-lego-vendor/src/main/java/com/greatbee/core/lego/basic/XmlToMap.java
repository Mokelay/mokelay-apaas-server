package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.Data;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.utils.VendorExceptionCode;
import com.greatbee.core.utils.XmlUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * LegoDemo
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("xmlToMap")
public class XmlToMap implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(XmlToMap.class);

    private static final String Input_Key_Xml_String = "xmlString";//需要转map的 xml字符串

    private static final String Output_Key_Xml_Map = "xmlData";//xml转换成的Data对象

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String xmlStr = input.getInputValue(Input_Key_Xml_String);
        logger.info("[XmlToMap] xmlStr = "+xmlStr);
        if(StringUtil.isInvalid(xmlStr)){
            throw new LegoException("XML转换字符串为空", VendorExceptionCode.Lego_Error_Xml_String_Null);
        }

        Data data = (Data) XmlUtil.xml2Map(xmlStr);
        //输出Data对象
        output.setOutputValue(Output_Key_Xml_Map,data);

        //设置单独的字段返回
        java.util.List<OutputField> ofs = output.getOutputField(IOFT.Memory);
        if(CollectionUtil.isValid(ofs)){
            for(OutputField of:ofs){
                if(data.containsKey(of.getFieldName())){
                    of.setFieldValue(data.get(of.getFieldName()));
                }
            }
        }

    }
}
