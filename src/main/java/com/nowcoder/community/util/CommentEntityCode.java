package com.nowcoder.community.util;

import lombok.Getter;

/**
 * @Author ztyh
 * @Description 查询评论时评论实体枚举类
 * @Date 2021/2/25 15:30
 */
@Getter
public enum CommentEntityCode {
    POST(1),
    COMMENT(2);

    private int code;

    CommentEntityCode(int code) {
        this.code = code;
    }
}
