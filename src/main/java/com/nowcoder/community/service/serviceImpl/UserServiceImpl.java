package com.nowcoder.community.service.serviceImpl;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.ActivationCode;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author ztyh
 * @Description UserService实现类
 * @Date 2021/2/8 22:13
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine; //thymeleaf模板引擎

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        if (null == user) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
        }

        //验证账号是否已存在
        if (userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }
        //验证邮箱是否已存在
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }
        //设置盐值，长度为5
        user.setSalt(CommunityUtil.getUUID().substring(0, 5));
        //md5摘要算法把密码设置为密文
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.getUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //url拼接（激活邮件中的激活url）
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "Lane激活账号", content);
        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            if (user.getStatus() == 1) {
                return ActivationCode.REPEAT.getStatus();
            } else if (user.getActivationCode().equals(code)) {
                userMapper.updateStatus(userId, 1);
                return ActivationCode.SUCCESS.getStatus();
            } else {
                return ActivationCode.FAILURE.getStatus();
            }
        }
        return ActivationCode.FAILURE.getStatus();
    }
}
