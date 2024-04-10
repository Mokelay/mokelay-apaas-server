package com.mokelay.core.lego.util;

import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Output;
import com.mokelay.core.manager.TYDriver;
import org.apache.log4j.Logger;

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
     * TODO 去掉核心库对Log数据库的依赖，写操作的日志记录不在lego执行过程中记录，方案待定 2024-04-10
     *
     * @param tyDriver
     * @param input
     * @param output
     * @param oi
     * @param lego
     */
    public static void saveLog(TYDriver tyDriver, Input input, Output output, OI oi, String lego) {
        /*
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
         */
    }

}