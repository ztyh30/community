package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @Author ztyh
 * @Description 登录注册模块工具类
 * @Date 2021/2/10 15:07
 */
public class CommunityUtil {

    //生成随机字符串
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5摘要算法
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
