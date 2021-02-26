package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

/**
 * @Author ztyh
 * @Description TODO
 * @Date 2021/2/25 15:37
 */
public interface CommentService {

    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int findCommentCountByEntity(int entityType, int entityId);

    int saveComment(Comment comment);
}
