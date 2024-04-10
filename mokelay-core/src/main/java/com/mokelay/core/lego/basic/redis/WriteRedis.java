package com.mokelay.core.lego.basic.redis;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.util.LegoUtil;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.db.util.RedisBuildUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 写入redis数据
 * <p>
 * 输入：
 * 1. 添加字段列表(多个,ioft=common)
 * 2. 过期时间 INPUT_KEY_EXPIRDATE
 * 输出：
 * Date: 2017/9/26
 */
@Component("writeRedis")
public class WriteRedis implements Lego, ExceptionCode {
    private static final Logger logger = Logger.getLogger(WriteRedis.class);

    private static final String INPUT_KEY_EXPIRDATE = "expirDate";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        String oiAlias = input.getApiLego().getOiAlias();

        long expirDate = DataUtil.getLong(input.getInputField(INPUT_KEY_EXPIRDATE).getFieldValue(), 30 * 24 * 60 * 60);//默认30天过期时间

        //通过 oiAlias获取OI
        OI oi;
        try {
            oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException(e.getMessage(), e, e.getCode());
        }

        String dsAlias = oi.getDsAlias();
        if (StringUtil.isInvalid(dsAlias)) {
            logger.error("dsAlias invalidate!");
            throw new LegoException("模型别名无效", ERROR_LEGO_DS_ALIAS_INVALIDATE);
        }
        try {
            java.util.List<InputField> fields = input.getInputField(IOFT.Cache);
            if (CollectionUtil.isValid(fields)) {
                RedisTemplate redisTemplate = RedisBuildUtil.getRedisTemplate(dsAlias);
                for (InputField field : fields) {
                    redisTemplate.opsForValue().set(
                            LegoUtil.transferInputValue(field.getFieldName(), LegoUtil.buildTPLParams(input.getRequest(), input.getInputFields(), null, input)),
                            field.getFieldValue(), expirDate, TimeUnit.SECONDS);
                }
            }
        } catch (DBException e) {
            logger.error(e.getMessage());
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }
}