package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

import java.util.Map;

/**
 * @Author ztyh
 * @Description UserService
 * @Date 2021/2/8 22:10
 */
public interface UserService {

    User findUserById(int id);

    Map<String, Object> register(User user);

    int activation(int userId, String code);
}
