package com.greatbee.core.lego.thirdparty;

import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.thirdparty.util.HmacUtil;
import com.greatbee.core.lego.wx.WxAuth;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * xiaoYiSign
 *
 * 小蚁签名 lego
 * input:
 *  appId
 *  enterpriseId,
 *  secret,
 *  apiPath,
 *  apiMethod
 *  非结构类型输入： Unstructured（条件）
 *
 * output:
 *  sign
 *
 * 根据 平台id  企业id   私钥  生成签名
 *
 * @author xiaobc
 * @date 18/9/05
 */
@Component("xiaoYiSign")
public class XiaoYiSign extends WxAuth{
    private static final Logger logger = Logger.getLogger(XiaoYiSign.class);

    private static final long ERROR_LEGO_XiaoYi_AppId_Error = 300200L;
    private static final long ERROR_LEGO_XiaoYi_Secret_Error = 300202L;
    private static final long ERROR_LEGO_XiaoYi_Api_Path_Error = 300203L;

    private static final String Input_Key_XiaoYi_AppId = "appId";//小蚁平台id
    private static final String Input_Key_XiaoYi_EnterpriseId = "enterpriseId";//小蚁企业id
    private static final String Input_Key_XiaoYi_Secret = "secret";//小蚁私钥

    private static final String Input_Key_XiaoYi_Api_Path = "apiPath";//授权请求接口路径 eg: /login/userRegisterByMobile
    private static final String Input_Key_XiaoYi_Api_Method = "apiMethod";//小蚁接口请求方式

    private static final String Input_Key_XiaoYi_Date = "currentDate";//接口请求时间时间

    private static final String Output_Key_XiaoYi_Sign = "sign";//返回签名

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String appId = input.getInputValue(Input_Key_XiaoYi_AppId);
        String enterpriseId = input.getInputValue(Input_Key_XiaoYi_EnterpriseId);
        String secret = input.getInputValue(Input_Key_XiaoYi_Secret);

        String apiPath = input.getInputValue(Input_Key_XiaoYi_Api_Path);
        String apiMethod = input.getInputValue(Input_Key_XiaoYi_Api_Method);

        String date = input.getInputValue(Input_Key_XiaoYi_Date);

        if(StringUtil.isInvalid(appId)){
            throw new LegoException("小蚁平台ID无效",ERROR_LEGO_XiaoYi_AppId_Error);
        }
        if(StringUtil.isInvalid(enterpriseId)){
            enterpriseId="";
        }
        if(StringUtil.isInvalid(secret)){
            throw new LegoException("小蚁密钥无效",ERROR_LEGO_XiaoYi_Secret_Error);
        }
        if(StringUtil.isInvalid(apiPath)){
            throw new LegoException("小蚁请求接口路径无效",ERROR_LEGO_XiaoYi_Api_Path_Error);
        }
        if(StringUtil.isInvalid(apiMethod)){
            apiMethod = "POST";//默认是POST 请求
        }

        //非结构输入  表示请求接口的请求参数
        List<InputField> list = input.getInputField(IOFT.Unstructured);

        String sign = sign(appId,secret,enterpriseId,apiMethod,apiPath,list,date);

        output.setOutputValue(Output_Key_XiaoYi_Sign,sign);
    }

    /**
     * 按字母顺序 拼接接口请求参数
     * •	参数名区分大小写，参数值为空不参与签名
     * @param list
     * @return
     */
    private static String getSortedQueryString(List<InputField> list) {
        Map<String,String> map = new HashMap<>();
        for(InputField _if:list){
            if(_if.getFieldValue()==null || StringUtil.isInvalid(DataUtil.getString(_if.getFieldValue(),""))){
                continue;
            }
            map.put(_if.getFieldName(), DataUtil.getString(_if.getFieldValue(),""));
        }
        Set<String> keys = map.keySet();
        List<String> keyList = Arrays.asList(
                keys.toArray(new String[keys.size()]));
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        for (String key : keyList) {
            String value = map.get(key);
            sb.append(String.format("&%s=%s", key,
                    value, "utf-8"));
        }
        return sb.length() > 0 ? sb.substring(1) : "";
    }


    /**
     * 小蚁签名
     * @param appId
     * @param appSecret
     * @param method
     * @param url
     * @param list  接口请求参数
     * @return
     */
    private static String sign(String appId,String appSecret,String enterpriseId,String method,String url, List<InputField> list,String date){
        if(StringUtil.isInvalid(date)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.format(new Date(System.currentTimeMillis()));
        }
        //拼接请求参数
        String sortedQueryString = getSortedQueryString(list);

        StringBuilder sb = new StringBuilder();
        String signString = sb.append(date).append(method).append(url)
                .append(sortedQueryString).toString();
        System.out.println(signString);
        logger.info("signString="+signString);
        String sign = HmacUtil.hmacSha1(appSecret, signString);
        System.out.println(sign);
        logger.info("sign=" + sign);
        String auth = "app_id=" + appId +"&enterprise_id="+ enterpriseId +"&signature=" + sign;
//        System.out.println(Base64Url.encode(auth, "utf8"));
        String result = Base64.getUrlEncoder().encodeToString(auth.getBytes());
        logger.info("xiaoyi result=" + result);
        return result;

    }


    //计算Authorization
//    public static void main(String[] args) {
//
//        String sign = HmacUtil.hmacSha1("T1V5wNkLNFbj8LZZY36dQAzfZvFnQlhbAmYJA451CPJbGbP8hVirswTRRxabn5lD", "2018-09-06 20:15:00POST/thirdparty/createEnterprise");
//        System.out.println(sign);

//
//        List<InputField> list = new ArrayList<>();
//        InputField _if1 = new InputField();
//        _if1.setFieldName("name");
//        _if1.setFieldValue("xiao");
//        list.add(_if1);
//
//        InputField _if2 = new InputField();
//        _if2.setFieldName("age");
//        _if2.setFieldValue(20);
//        list.add(_if2);
//
//        InputField _if3 = new InputField();
//        _if3.setFieldName("sex");
//        _if3.setFieldValue("");
//        list.add(_if3);
//        System.out.println(getSortedQueryString(list));
//
//        String appId= "smartgo9900001";
//        String appSecret = "T1V5wNkLNFbj8LZZY36dQAzfZvFnQlhbAmYJA451CPJbGbP8hVirs";
//        String method="POST";
//        String url = "/login/userRegisterByMobile";
//        String enterpriseId = "9527";
//        String sign = sign(appId,appSecret,enterpriseId,method,url,list);
//        System.out.println("sign="+sign);
//    }



}
