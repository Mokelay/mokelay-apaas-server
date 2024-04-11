package com.mokelay.vendor.lego.basic;

import com.mokelay.base.bean.Data;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.vendor.utils.VendorExceptionCode;
import com.mokelay.vendor.utils.XmlUtil;
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
