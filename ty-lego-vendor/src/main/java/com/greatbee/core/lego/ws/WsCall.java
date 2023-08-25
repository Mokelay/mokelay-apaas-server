package com.greatbee.core.lego.ws;

import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.utils.VendorExceptionCode;
import com.greatbee.core.utils.WsUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * web service 调用 lego   可以调用任何 wsdl 的web service接口
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wsCall")
public class WsCall implements ExceptionCode, Lego{

    private static final Logger logger = Logger.getLogger(WsCall.class);

    private static final String Input_Key_Wsdl_Url = "wsdlUrl";//wsdl 接口文档地址
    private static final String Input_Key_Request_Body = "requestBody";//请求体 请求参数(eg: {a:123,b:321})
    private static final String Input_Key_Target_Namespace = "targetNamespace";//命名空间  eg:http://tempuri.org/
    private static final String Input_Key_Soap_Action = "soapAction";//soap接口地址
    private static final String Input_Key_Method = "method";//方法名

    private static final String Output_Key_Ws_Response = "wsResponse";//ws 接口返回字符串

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String url = input.getInputValue(Input_Key_Wsdl_Url);
        String xmlData = input.getInputValue(Input_Key_Request_Body);//非必填
        String targetNamespace = input.getInputValue(Input_Key_Target_Namespace);
        String soapAction = input.getInputValue(Input_Key_Soap_Action);
        String method = input.getInputValue(Input_Key_Method);

        if(StringUtil.isInvalid(url)||StringUtil.isInvalid(targetNamespace)||StringUtil.isInvalid(soapAction)||StringUtil.isInvalid(method)){
            throw new LegoException("web service接口请求参数无效", VendorExceptionCode.Lego_Error_Web_Service_Config_Param_Null);
        }
        String result = WsUtil.invokeWsdl(url, xmlData, targetNamespace, soapAction, method);
        logger.info("WsCall result="+result);
        output.setOutputValue(Output_Key_Ws_Response,result);
    }
}
