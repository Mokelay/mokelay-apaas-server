package com.greatbee.vendor.lego.wx;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.vendor.lego.wx.util.WxMpUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * WxGetOpenId
 *
 * 微信获取OpenId lego   这个接口应该是一个get接口
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("wxGetOpenId")
public class WxGetOpenId extends WxAuth{
    private static final Logger logger = Logger.getLogger(WxGetOpenId.class);

    private static final String Input_Key_Wx_Is_Miniprogram = "isMp";//是否是小程序

    private static final String Output_Key_WX_Open_Id = "openId";//返回微信openId

    private static final String Output_Key_Session_Id = "si";//返回sessionId，因为小程序每次请求的session都不同，前端需要手动设置sessionId  安全起见，字段名就简化

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑

        boolean isMp = DataUtil.getBoolean(input.getInputValue(Input_Key_Wx_Is_Miniprogram),false);
        String code = input.getRequest().getParameter("code");
//        String state = input.getRequest().getParameter("state");
        logger.info("[wxGetOpenId] code = "+code);
//        logger.info("[wxGetOpenId] state = " + state);
        String appId = input.getInputValue(Input_Key_Wx_Open_App_Id);
        String secret = input.getInputValue(Input_Key_WX_Open_Secret);
        if(StringUtil.isInvalid(appId)||StringUtil.isInvalid(secret)){
            throw new LegoException("微信参数缺失",ERROR_LEGO_WX_Params_Null);
        }

        String openID = null;
        try {
            if(isMp){
                //是小程序
                openID = WxMpUtil.getMpOpenId(appId,secret,code);
            }else{
                openID = this.getOpenId(code,appId,secret);
            }
            logger.info("[wxGetOpenId] oepnID=" + openID);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        output.setOutputValue(Output_Key_WX_Open_Id,openID);
        output.setOutputValue(Output_Key_Session_Id,input.getRequest().getSession().getId());
    }


}
