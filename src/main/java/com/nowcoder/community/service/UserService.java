package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

/**
 * @Author ztyh
 * @Description UserService
 * @Date 2021/2/8 22:10
 */
public interface UserService {

    User findUserById(int id);
}
