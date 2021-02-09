package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author ztyh
 * @Description DiscussPostMapper
 * @Date 2021/2/8 20:37
 */
@Repository
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //如果只有一个参数，并且在<if>标签内使用，则必须起别名
    int selectDiscussPostRows(@Param("userId") int userId);


}
