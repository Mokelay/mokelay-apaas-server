package com.greatbee.core.lego.message;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.greatbee.core.ExceptionCode;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

/**
 * 数组合并
 * <p/>
 * 输入：
 * 1.
 * 输出：
 * 1.
 * <p/>
 * Author: CarlChen
 * Date: 2017/5/31
 */
@Component("aliyunSendSMS")
public class AliyunSendSMS implements Lego, ExceptionCode {
    public static final long ERROR_LEGO_SEND_SMS_ERROR = 310001;

    private static final String Input_Key_AccessKeyId = "accessKey_id";
    private static final String Input_Key_AccessKeySecret = "access_key_secret";
    private static final String Input_Key_Phone_Numbers = "phone_numbers";
    private static final String Input_Key_Sign_Name = "sign_name";
    private static final String Input_Key_Template_Code = "template_code";
    private static final String Input_Key_Template_Param = "template_param";

    private static final String Output_Key_Code = "code";
    private static final String Output_Key_Message = "message";
    private static final String Output_Key_RequestId = "request_id";
    private static final String Output_Key_Biz_Id = "biz_id";


    @Override
    public void execute(Input input, Output output) throws LegoException {
        try {
            String accessKeyId = input.getInputValue(Input_Key_AccessKeyId);
            String accessKeySecret = input.getInputValue(Input_Key_AccessKeySecret);

            String phoneNumbers = input.getInputValue(Input_Key_Phone_Numbers);
            String signName = input.getInputValue(Input_Key_Sign_Name);
            String templateCode = input.getInputValue(Input_Key_Template_Code);
            String templateParam = input.getInputValue(Input_Key_Template_Param);

            SendSmsResponse response = sendSms(accessKeyId, accessKeySecret, phoneNumbers, signName, templateCode, templateParam);

            output.setOutputValue(Output_Key_Code, response.getCode());
            output.setOutputValue(Output_Key_Message, response.getMessage());
            output.setOutputValue(Output_Key_RequestId, response.getRequestId());
            output.setOutputValue(Output_Key_Biz_Id, response.getBizId());

        } catch (ClientException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, ERROR_LEGO_SEND_SMS_ERROR);
        }
    }

    public static void main(String[] args) throws ClientException, InterruptedException {
        //发短信
//        SendSmsResponse response = sendSms("18516246910", "TY", "SMS_134205252", "{\"name\":\"Tom\", \"code\":\"123\"}");
//        System.out.println("短信接口返回的数据----------------");
//        System.out.println("Code=" + response.getCode());
//        System.out.println("Message=" + response.getMessage());
//        System.out.println("RequestId=" + response.getRequestId());
//        System.out.println("BizId=" + response.getBizId());

//        Thread.sleep(3000L);

        //查明细
//        if (response.getCode() != null && response.getCode().equals("OK")) {
//            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
//            System.out.println("短信明细查询接口返回数据----------------");
//            System.out.println("Code=" + querySendDetailsResponse.getCode());
//            System.out.println("Message=" + querySendDetailsResponse.getMessage());
//            int i = 0;
//            for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
//                System.out.println("SmsSendDetailDTO[" + i + "]:");
//                System.out.println("Content=" + smsSendDetailDTO.getContent());
//                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
//                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
//                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
//                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
//                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
//                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
//                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
//            }
//            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
//            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
//        }

    }

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String Product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String Domain = "dysmsapi.aliyuncs.com";

    /**
     * 发送短信
     *
     * @param accessKeyId     AK
     * @param accessKeySecret AKS
     * @param phoneNumbers    手机号
     * @param signName        签名
     * @param templateCode    模板代码
     * @param templateParam   模板变量
     * @return
     * @throws ClientException
     */
    private static SendSmsResponse sendSms(String accessKeyId,
                                           String accessKeySecret,
                                           String phoneNumbers,
                                           String signName,
                                           String templateCode,
                                           String templateParam) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Product, Domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }

//    private static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {
//
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Product, Domain);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//
//        //组装请求对象
//        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
//        //必填-号码
//        request.setPhoneNumber("15000000000");
//        //可选-流水号
//        request.setBizId(bizId);
//        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
//        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
//        request.setSendDate(ft.format(new Date()));
//        //必填-页大小
//        request.setPageSize(10L);
//        //必填-当前页码从1开始计数
//        request.setCurrentPage(1L);
//
//        //hint 此处可能会抛出异常，注意catch
//        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
//
//        return querySendDetailsResponse;
//    }
}
