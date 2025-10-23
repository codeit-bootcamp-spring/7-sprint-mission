package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
public interface MessageService {
    Message createMessage(String content, UUID channelId, UUID userId);
    Message getMessage(UUID messageId);
    List<Message> getAllMessages();
    void updateMessage(UUID messageId, String content);
    void deleteMessage(UUID messageId);
    void deleteMessagesByChannel(UUID channelId); // channelID 해당 메세지 전부 삭제
    void deleteMessagesByUser(UUID userId); // userID 해당 메세지 전부 삭제
}
