package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;

import java.util.List;

/**
 * @Author ztyh
 * @Description TODO
 * @Date 2021/3/3 19:35
 */
public interface MessageService {

    List<Message> findConversations(int userId, int offset, int limit);

    int findConversationCount(int userId);

    List<Message> findLetters(String conversationId, int offset, int limit);

    int findLetterCount(String conversationId);

    int findLetterUnreadCount(int userId, String conversationId);
}
