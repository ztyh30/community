package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @Author ztyh
 * @Description UserMapper
 * @Date 2021/2/8 17:38
 */
@Repository
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
