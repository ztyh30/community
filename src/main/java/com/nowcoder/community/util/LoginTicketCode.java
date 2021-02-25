package com.nowcoder.community.util;

import lombok.Getter;

/**
 * @Author ztyh
 * @Description 枚举类，用户登录凭证过期时间
 * @Date 2021/2/16 14:58
 */
@Getter
public enum LoginTicketCode {
    DEFAULT_EXPIRED_SECONDS(3600 * 12),
    REMEMBER_EXPIRED_SECONDS(3600 * 24 * 100);

    private int seconds;

    LoginTicketCode(int seconds) {
        this.seconds = seconds;
    }
}
