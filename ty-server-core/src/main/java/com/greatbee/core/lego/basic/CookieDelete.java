package com.greatbee.core.lego.basic;

import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.CookieUtil;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Cookie销毁
 * <p/>
 * 输入：需要删除的Cookie字段列表
 * <p/>
 * 输出：暂无
 * <p/>
 * Author :CarlChen
 * Date:17/7/17
 */
@Component("cookieDelete")
public class CookieDelete implements Lego {
    @Override
    public void execute(Input input, Output output) throws LegoException {
        java.util.List<InputField> removeFields = input.getInputFields();

        if (CollectionUtil.isValid(removeFields)) {
            for (InputField field : removeFields) {
                Cookie cookie = CookieUtil.getCookie(input.getRequest(), field.getFieldName());
                if (cookie != null) {
                    CookieUtil.deleteCookie(input.getRequest(), input.getResponse(), cookie);
                }
            }
        }
    }
}
