package com.nowcoder.community.util;

import lombok.Getter;

/**
 * @Author ztyh
 * @Description 枚举类，激活类型
 * @Date 2021/2/12 23:55
 */
@Getter
public enum ActivationCode {
    SUCCESS(0, "激活成功"),
    REPEAT(1, "重复激活！该激活码已激活过！"),
    FAILURE(2, "激活失败！该激活码有误！");

    private int status;
    private String message;

    ActivationCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
