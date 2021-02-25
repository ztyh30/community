package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author ztyh
 * @Description Cookie工具类
 * @Date 2021/2/18 21:59
 */
public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
