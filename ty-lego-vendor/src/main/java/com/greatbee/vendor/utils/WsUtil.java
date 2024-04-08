package com.greatbee.vendor.utils;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

/**
 * WsUtil
 *
 *  web service client 工具类
 * @author xiaobc
 * @date 18/10/24
 */
public class WsUtil {

    private static Logger logger = Logger.getLogger(WsUtil.class);

    /**
     * web service调用
     * @param url
     * @param xmlData   xml的请求参数  body内容  默认参数是requestXML
     * @param targetNamespace
     * @param soapAction
     * @param method
     * @return
     */
    public static String invokeWsdl(String url,String xmlData,String targetNamespace,String soapAction,String method){
        Service service = new Service();
        try {
            Call call2 = (Call) service.createCall();
            call2.setTargetEndpointAddress(url);
            call2.setUseSOAPAction(true);
            call2.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));//设置返回参数
            call2.setOperationName(new QName(targetNamespace, method));//设置函数名
            call2.setSOAPActionURI(soapAction);//设置URI
            call2.addParameter(new QName(targetNamespace, getWebParamName(method)), XMLType.XSD_STRING, ParameterMode.IN);  // 这里设置对应参数名称
            String retVal2 = (String) call2.invoke(new Object[] { xmlData });  //调用并带上参数数据
            System.out.println("[invokeWsdl] result = "+retVal2);
            return retVal2;
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("[invokeWsdl] web service 调用失败!"+e.getMessage());
        }
        return null;
    }

    /**
     * 获取 web service 参数名
     * @param method
     * @return
     */
    private static String getWebParamName(String method){
        switch(method){
            case "PrePayBalanceQuery":
                return "requestJson";
            case "SubMchQuery":
                return "requestJson";
            case "CancelEOCoupon":
                return "requestJson";
            case "CouponPurchase":
                return "requestXml";
            case "OrderQuery":
                return "requestXml";
            case "GetCouponTypeList":
                return "requestXml";
            case "CouponQuery":
                return "requestXml";
            default:
                return "requestXml";
        }
    }


    //    测试
    public static void main(String[] args) {
        String encodeParams = "{\"SignKey\":\"DlnoLEynWM+tfe2pRZ/Ta7e7RRf98oIyTU2JXBnIJOeBkez0oDB7tuGwBwI1dwM10v/inrkVoRp/sUPjEZsRlD9YfIM+qv1NJ7tYbTUMmdF7tUD4h/WlUekk3fFF8fZKaGaNujx6Q1bsSSCnwzJgRHPNlokjYK7x7IowHfk9g5k=\",\"Data\":\"ALwm6rXJRlyB7HMKyAFNaDmRjtPb4II1pQ0Yw1hqorCx7FCpwJKemfErlvBEGM6HPb+G37DvbzvOL8TkBtTMkJXj+nfGKsmVY0OIXdoscX+z/KNJqURp/Y5ejECnnISnT559BIOEHti66rnT88pHbNIBelcKrwIIlfYITrQ+zzaXPE3twDZ3kQ==\",\"CustomerCode\":\"0010000010\"}";

        String url = "http://61.140.21.164:65501/GDTest/EOPartnerService.svc?wsdl" ;
        String xmlData= encodeParams;
        Service service2 = new Service();

        try {
            Call call2 = (Call) service2.createCall();
            call2.setTargetEndpointAddress(url);
            call2.setUseSOAPAction(true);
            call2.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));//设置返回参数
            call2.setOperationName(new QName("http://tempuri.org/", "PrePayBalanceQuery"));//设置函数名
            call2.setSOAPActionURI("http://tempuri.org/IEOPartnerService/PrePayBalanceQuery");//设置URI
            call2.addParameter(new QName("http://tempuri.org/", "requestXML"), XMLType.XSD_STRING, ParameterMode.IN);  // 这里设置对应参数名称
            String retVal2 = (String) call2.invoke(new Object[] { xmlData });  //调用并带上参数数据
            System.out.println(retVal2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
