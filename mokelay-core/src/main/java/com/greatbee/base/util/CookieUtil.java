package com.greatbee.base.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie Util
 *
 * @author CarlChen
 * @version 1.00 2007-9-24 16:53:30
 */
public class CookieUtil
{
    /**
     * Get Cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                return cookies[i];
            }
        }
        return null;
    }

    /**
     * ��ȡCookie��ֵ
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name)
    {
        String value = null;
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            //���Ľ���
            try {
                value = URLDecoder.decode(cookie.getValue(), Charset.UTF_8);
            }
            catch (UnsupportedEncodingException e) {
                value = cookie.getValue();
            }
        }
        return value;
    }

    /**
     * Delelte Cookie
     *
     * @param request
     * @param response
     * @param cookie
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, Cookie cookie)
    {
        if (cookie != null) {
            cookie.setPath(getPath(request));
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * Set Cookie
     *
     * @param request
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value)
    {
        String cookieValue = null;
        try {
            cookieValue = URLEncoder.encode(value, Charset.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            cookieValue = value;
        }
        setCookie(request, response, name, cookieValue, 0x278d00);
    }

    /**
     * Set Cookie
     *
     * @param request
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value, int maxAge)
    {
        Cookie cookie = new Cookie(name, value == null ? "" : value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getPath(request));
//        cookie.setDomain(request.getServerName());
        response.addCookie(cookie);
    }

    /**
     * ��cookie�л�ȡ����
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String[] getArrayFromCookie(HttpServletRequest request, String cookieName)
    {
        String value = getCookieValue(request, cookieName);
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    /**
     * ������д��cookie
     *
     * @param request
     * @param response
     * @param cookieName
     * @param values
     */
    public static void setArrayToCookie(HttpServletRequest request,
                                        HttpServletResponse response, String cookieName, String[] values)
    {
        if (ArrayUtil.isInvalid(values)) {
            return;
        }
        StringBuffer cookieValueBuf = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            if (StringUtil.isValid(values[i])) {
                cookieValueBuf.append(values[i]);
                if (i != values.length - 1) {
                    cookieValueBuf.append(",");
                }
            }
        }
        setCookie(request, response, cookieName, cookieValueBuf.toString());
    }

    /**
     * Get Path
     *
     * @param request
     * @return
     */
    private static String getPath(HttpServletRequest request)
    {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }

}
