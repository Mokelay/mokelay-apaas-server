package com.greatbee.vendor.lego.basic;

import com.greatbee.base.bean.Data;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.vendor.utils.VendorExceptionCode;
import com.greatbee.vendor.utils.XmlUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * MapToXml
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("mapToXml")
public class MapToXml implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(MapToXml.class);

    private static final String Input_Key_Data = "data";//需要转xml字符串的 Data对象(map)

    private static final String Output_Key_Xml_String = "xmlString";//Data对象转换成的xml字符串

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        Data data = (Data) input.getInputObjectValue(Input_Key_Data);
        if(data==null){
            throw new LegoException("XML转换无效参数", VendorExceptionCode.Lego_Error_Xml_Data_Null);
        }

        String xmlStr = XmlUtil.map2Xml(data);
        logger.info("[MapToXml] xmlStr = " + xmlStr);
        //输出Data对象
        output.setOutputValue(Output_Key_Xml_String,xmlStr);

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
