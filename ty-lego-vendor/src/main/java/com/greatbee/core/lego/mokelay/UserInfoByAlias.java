package com.greatbee.core.lego.mokelay;

import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.manager.TYDriver;
import com.greatbee.core.util.DataSourceUtils;
import com.greatbee.core.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 根据alias 读取mokelay 用户表 信息
 * 输入：
 *  alias
 * 输入：
 *  用户对象，
 *  单个用户信息字段(常用字段)
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("userInfoByAlias")
public class UserInfoByAlias implements ExceptionCode, Lego {
    private static final Logger logger = Logger.getLogger(UserInfoByAlias.class);

    private static final String Input_Key_Mokelay_User_alias = "userAlias";//用户表别名

    private static final String Output_Key_User_Data = "data";//返回的data

    @Autowired
    TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String userAlias = input.getInputValue(Input_Key_Mokelay_User_alias);
        if(StringUtil.isInvalid(userAlias)){
            throw new LegoException("用户别名参数无效", VendorExceptionCode.Lego_Error_Mokelay_Param_Null);
        }
        String sql = "select * from `ty_user` where alias=?";
        JdbcTemplate tyTemplate = getTyTemplate();
        Map<String,Object> result = tyTemplate.queryForMap(sql,userAlias);

        output.setOutputValue(Output_Key_User_Data,result);

        List<OutputField> outs = output.getOutputField(IOFT.Common);
        for(OutputField of:outs){
            if(!Output_Key_User_Data.equalsIgnoreCase(of.getFieldName()) && StringUtil.isValid(of.getFieldName())
                    &&result.containsKey(of.getFieldName())){
                of.setFieldValue(result.get(of.getFieldName()));
            }
        }

    }

    /**
     * 返回ty的jdbcTemplate
     * @return
     */
    private JdbcTemplate getTyTemplate(){
        DataSource dataSource = DataSourceUtils.getDatasource("db_ty", tyDriver.getDsManager());
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }


}
