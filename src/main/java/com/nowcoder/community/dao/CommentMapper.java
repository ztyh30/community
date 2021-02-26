package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author ztyh
 * @Description CommentMapper
 * @Date 2021/2/25 15:10
 */
@Repository
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCommentCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);
}
