package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author ztyh
 * @Description 测试敏感词过滤
 * @Date 2021/2/23 22:34
 */
@SpringBootTest
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Test
    public void testFilter() {
        String text = "傻逼傻逼，傻东东，操你妈的，哈哈哈";
        String res = sensitiveFilter.filter(text);
        System.out.println(res);
        text = "傻★逼，★傻东东，操★你★妈的，哈哈哈";
        res = sensitiveFilter.filter(text);
        System.out.println(res);
    }
}
