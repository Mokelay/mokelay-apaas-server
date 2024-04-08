package com.greatbee.core.lego.util;

import com.alibaba.fastjson.JSONArray;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.api.bean.server.APILego;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.bean.server.OutputField;
import com.greatbee.core.bean.user.Log;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Output;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.utils.SessionUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * LogUtil
 *
 * @author xiaobc
 * @date 17/11/15
 */
public class LogUtil {

    private static Logger logger = Logger.getLogger(LogUtil.class);

    /**
     * 保存日志
     *
     * @param tyDriver
     * @param input
     * @param output
     * @param oi
     * @param lego
     */
    public static void saveLog(TYDriver tyDriver, Input input, Output output, OI oi, String lego) {
        try {
            if (oi == null || !oi.getDsAlias().equalsIgnoreCase("db_ty")) {
                //只记录ty库的操作记录
                return;
            }
            Log log = new Log();
            HttpServletRequest req = input.getRequest();
            String userAlias = null;
            String userName = null;
            if (req != null) {
                //客户端请求执行
                userAlias = SessionUtil.getAttributeValue(req, SessionUtil.E_SESSION_USER_DETAIL_KEY, SessionUtil.E_SESSION_USER_ID);
                userName = SessionUtil.getAttributeValue(req, SessionUtil.E_SESSION_USER_DETAIL_KEY, SessionUtil.E_SESSION_USER_NAME);
            } else {
                //由JOB执行
                userAlias = "JOB";
                userName = "JOB";
            }
            APILego al = input.getApiLego();

            log.setOperateUserAlias(userAlias);
            log.setOperateUserName(userName);
            log.setOperateLego(lego);
            log.setOperateOi(oi.getAlias());
            log.setCreateDate(new Date());
            log.setOperateApi(al.getApiAlias());

            List<InputField> ifs = input.getInputFields();
            List<OutputField> ofs = output.getOutputFields();

            log.setInput(JSONArray.toJSONString(ifs));
            log.setOutput(JSONArray.toJSONString(ofs));
            tyDriver.getLogManager().add(log);
        } catch (Exception e) {
            logger.error("saveLog invalid", e);
        }
    }

}
