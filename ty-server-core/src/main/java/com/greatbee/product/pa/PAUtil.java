package com.greatbee.product.pa;

import javax.servlet.http.HttpServletRequest;

/**
 * 私有化授权
 * Privatization Authorization
 * <p/>
 * Author: CarlChen
 * Date: 2018/3/13
 */
public class PAUtil {
    private static final String PA_SN = "T_Y_P_A_S_N_2018";
    private static final String PA_KEY = "T_Y_P_A_K_E_Y_2018";

    public static void main(String args[]) {
    }

    /**
     * 根据请求授权
     *
     * @param request
     * @return
     */
    public static boolean auth(HttpServletRequest request) {
        //TODO
        return true;
    }
}
