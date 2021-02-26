package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;

import java.util.List;

/**
 * @Author ztyh
 * @Description DiscussPostService
 * @Date 2021/2/8 22:05
 */
public interface DiscussPostService {

    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);

    int findDiscussPostRows(int userId);

    int addDiscussPost(DiscussPost discussPost);

    DiscussPost findDiscussPostById(int id);

    int updateCommentCount(int count, int id);
}
