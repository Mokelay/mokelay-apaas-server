package com.mokelay.vendor.lego.crypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelay.base.bean.Data;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.api.bean.server.OutputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.vendor.utils.VendorExceptionCode;
import com.mokelay.vendor.utils.WsUtil;
import com.mokelay.vendor.utils.XmlUtil;
import com.mokelay.vendor.utils.fyCrypt.RsaUtilExt;
import com.mokelay.vendor.utils.fyCrypt.ThreeDES;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * FyWsInvoke fy 接口加密
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("fyWsInvoke")
public class FyWsInvoke implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(FyWsInvoke.class);

    private static final String Sign_Key="SignKey";
    private static final String Fy_Data = "Data";


    private static final String Input_Key_Ws_Wsdl_Url = "wsdlUrl";//wsdl地址
    private static final String Input_Key_Ws_Target_Namespace = "targetNamespace";//wsdl 命名空间
    private static final String Input_Key_Ws_Soap_Action = "soapAction";//soap请求地址
    private static final String Input_Key_Ws_Wsdl_Method = "wsdlMethod";//ws接口方法名
    private static final String Input_Key_Static_3DESKey="static3DesKey";//静态的3deskey
    private static final String Input_Key_Rsa_Public_Key="rsaPublicKey";//对方 rsa 公钥
    private static final String Input_Key_My_Rsa_Private_Key = "myRsaPrivateKey";//我方(客户)的 rsa 私钥
    private static final String Input_Key_Req_Data = "reqData";//请求对象   通过对象重组合并成接口需要的对象，  会将data转成xml字符串

    private static final String Input_Key_Customer_Code = "customerCode";//客户编码 唯一标示

    private static final String Output_Key_Res_Data = "resData";//返回对象

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String static3DesKey = input.getInputValue(Input_Key_Static_3DESKey);
        String rsaPublicKey = input.getInputValue(Input_Key_Rsa_Public_Key);//可以是.net结构的秘钥<RSAKeyValue><Modulus>...</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>
        String myRsaPrivateKey = input.getInputValue(Input_Key_My_Rsa_Private_Key);//我方私钥
        String customerCode = input.getInputValue(Input_Key_Customer_Code);//客户编码
        if(StringUtil.isInvalid(static3DesKey)){
            throw new LegoException("请设置客户静态3DES秘钥", VendorExceptionCode.Lego_Error_Fy_Static_3DES_Null);
        }
        if(StringUtil.isInvalid(rsaPublicKey)){
            throw new LegoException("请设置对方RSA公钥，支持.net格式",VendorExceptionCode.Lego_Error_Fy_RSA_Public_Key_Null);
        }
        if(StringUtil.isInvalid(customerCode)){
            throw new LegoException("请传入客户编码",VendorExceptionCode.Lego_Error_Fy_Customer_Code_Null);
        }
        if(StringUtil.isInvalid(myRsaPrivateKey)){
            throw new LegoException("请输入客户rsa私钥",VendorExceptionCode.Lego_Error_Fy_Customer_RSA_Private_Key_Null);
        }
        String wsdlUrl = input.getInputValue(Input_Key_Ws_Wsdl_Url);
        String targetNamespace = input.getInputValue(Input_Key_Ws_Target_Namespace);
        String soapAction = input.getInputValue(Input_Key_Ws_Soap_Action);
        String wsdlMethod = input.getInputValue(Input_Key_Ws_Wsdl_Method);

        if(StringUtil.isInvalid(wsdlUrl)||StringUtil.isInvalid(targetNamespace)||StringUtil.isInvalid(soapAction)||StringUtil.isInvalid(wsdlMethod)){
            throw new LegoException("web service接口请求参数无效", VendorExceptionCode.Lego_Error_Web_Service_Config_Param_Null);
        }
        //获取请求参数
        Data data = (Data) input.getInputObjectValue(Input_Key_Req_Data);

        //将data对象转成xml字符串
        String reqXml =  XmlUtil.map2Xml(data,"req");//根节点名称为 req
        logger.info("[FyWsInvoke] reqXml = " + reqXml);

        //生成动态3des 秘钥
        String dynamicDesKey = buildDynamic3Des();
        //加密动态3des秘钥 生成 signKey
        PublicKey rsaPk = null;
        if(rsaPublicKey.contains("<RSAKeyValue>")){
            HashMap<String,String> pmap = null;
            try {
                pmap = RsaUtilExt.rsaParameters(rsaPublicKey);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
            } catch (DocumentException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
            }
            rsaPk = RsaUtilExt.getPublicKey(pmap.get("mudulus"), pmap.get("exponent"));
        }else{
            logger.error("公钥设置错误");
            throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
        }
        String signKey = null;
        try {
            signKey = RsaUtilExt.encodeBase64(RsaUtilExt.encryptByPublicKey(dynamicDesKey.getBytes("UTF-8"), RsaUtilExt.encodeBase64(rsaPk.getEncoded())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new LegoException("RSA加密失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
        }
        //Data 数据加密  先静态3des加密，再用动态3des加密
        String staticCiphertext = ThreeDES.encryption(static3DesKey, reqXml);
        String ciphertext = ThreeDES.encryption(dynamicDesKey, staticCiphertext);

        //组装请求的机密json
        Data req = new Data();
        req.put(Sign_Key,signKey);
        req.put(Fy_Data,ciphertext);
        req.put("CustomerCode", customerCode);

        logger.info("wsdl接口请求数据-动态16位3des秘钥：" + dynamicDesKey);
        logger.info("wsdl接口请求数据：" + JSON.toJSONString(req));
        //发送请求
        String resStr = WsUtil.invokeWsdl(wsdlUrl, JSON.toJSONString(req),targetNamespace,soapAction,wsdlMethod);
        logger.info("wsdl接口请求返回数据："+resStr);

        //返回的resStr 是加密后的字符串，需要解密
        JSONObject resJson = JSONObject.parseObject(resStr);
        String resSignKey = resJson.getString(Sign_Key);
        String resData = resJson.getString(Fy_Data);

        if(StringUtil.isInvalid(resSignKey)){
            //接口报错了
            Map<String,Object> resMap = XmlUtil.xml2Map(resData);
            throw new LegoException("错误码:"+resMap.get("RetCode")+";流水号:"+resMap.get("FlowNo")+";错误信息:"+resMap.get("RetMsg"),VendorExceptionCode.Lego_Error_Fy_Web_Service_Error);
        }

        //解密后的明文 xml文件
        String resXml = null;
        try {
            //先用rsa私钥取出动态3des秘钥，
//            resXml = CryptUtil.privateDecrypt(resSignKey, CryptUtil.getPrivateKey(myRsaPrivateKey));//私钥只支持java版的
            String res_signKey = new String(RsaUtilExt.decryptByPrivateKey(RsaUtilExt.decodeBase64(resSignKey), myRsaPrivateKey));//私钥只支持java版的

            //解密Data：用取出的动态3des秘钥解密， 再用静态的3des秘钥解密
            String dynamicRes = ThreeDES.decryption(res_signKey, resData);
            resXml = ThreeDES.decryption(static3DesKey, dynamicRes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LegoException("秘钥获取失败",VendorExceptionCode.Lego_Error_Fy_RSA_Key_Error);
        }

        //明文xml文件转成Data对象
        Map resMap = XmlUtil.xml2Map(resXml);

        output.setOutputValue(Output_Key_Res_Data,resMap);

        //设置单独的字段返回
        java.util.List<OutputField> ofs = output.getOutputField(IOFT.Memory);
        if(CollectionUtil.isValid(ofs)){
            for(OutputField of:ofs){
                if(resMap.containsKey(of.getFieldName())){
                    of.setFieldValue(resMap.get(of.getFieldName()));
                }
            }
        }

    }

    //生成动态的16位3DES秘钥 随机数
    private String buildDynamic3Des(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0,16);
    }
}
