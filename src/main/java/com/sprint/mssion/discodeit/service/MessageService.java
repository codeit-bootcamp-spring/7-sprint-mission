package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
public interface MessageService {
    Message createMessage(String message, UUID channelId, UUID userId);
    Message getMessage(UUID messageId);
    List<Message> getAllMessages();
    void updateMessage(UUID messageId, String message);
    void deleteMessage(UUID messageId);
    void deleteMessagesByChannel(UUID channelId); // channelID 해당 메세지 전부 삭제
    void deleteMessagesByUser(UUID userId); // userID 해당 메세지 전부 삭제
}
