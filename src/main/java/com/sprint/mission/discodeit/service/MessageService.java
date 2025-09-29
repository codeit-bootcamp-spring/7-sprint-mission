package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {

    void createMessage(Message message);
    void readMessage(Message message);
    void readAllMessage();
    void deleteMessage(Message message);
    <T> void updateMessage(Message message, Message.messageElement messageElement,T updatedContent);
    void readUpdatedMessage();
    void readDeletedMessage();
}
