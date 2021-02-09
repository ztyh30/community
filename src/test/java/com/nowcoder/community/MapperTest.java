package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author ztyh
 * @Description 测试UserMapper
 * @Date 2021/2/8 18:01
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testUserSelect() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");

        System.out.println(user);
    }

    @Test
    public void testUserInsert() {
        User user = new User();
        user.setUsername("zyh");
        user.setEmail("ztyh19990905_@163.com");
        user.setPassword("123456");
        user.setSalt("sdwd");
        System.out.println("Insert Row:"+userMapper.insertUser(user));
    }

    @Test
    public void testUserUpdate() {
        System.out.println("Update Row:"+userMapper.updatePassword(150, "123321"));
    }

    @Test
    public void testDiscussPostSelect() {
        List<DiscussPost> posts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : posts) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println("总帖数："+rows);

    }

}
