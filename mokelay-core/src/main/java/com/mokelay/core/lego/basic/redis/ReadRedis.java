package com.mokelay.core.lego.basic.redis;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.base.util.CollectionUtil;
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

/**
 * 读取redis数据    配置需要注意一点， 输出数据如果别名相同的话，后创建的输出字段会覆盖先创建的字段(outputField)
 * <p/>
 * 输入：
 * 1. 添加字段列表(多个,ioft=common)
 * 输出：
 * 1.输出数据 OUTPUT_DATA_KEY(map对象)
 * Date: 2017/9/26
 */
@Component("readRedis")
public class ReadRedis implements Lego, ExceptionCode {
    private static final Logger logger = Logger.getLogger(ReadRedis.class);

    private static final String OUTPUT_DATA_KEY = "data";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        String oiAlias = input.getApiLego().getOiAlias();

        Data data = new Data();
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
                    Object item = redisTemplate.opsForValue().get(
                            LegoUtil.transferInputValue(field.fieldValueToString(),
                                    LegoUtil.buildTPLParams(input.getRequest(), input.getInputFields(), null, input))
                    );
                    if (item != null) {
                        data.put(field.getAlias(), item);
                        output.setOutputValue(field.getAlias(), item);
                    }
                }
            }
            output.setOutputValue(OUTPUT_DATA_KEY, data);

        } catch (DBException e) {
            logger.error(e.getMessage());
            throw new LegoException(e.getMessage(), e, e.getCode());
        }
    }
}
