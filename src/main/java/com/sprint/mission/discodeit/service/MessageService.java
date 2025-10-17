package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {

    public void createMessage(MessageDto messageDto);
    public void readMessage(MessageDto messageDto);
    public void readAllMessage();
    public void deleteMessage(MessageDto messageDto);
    public void updateMessage(MessageDto messageDto, Message.messageElement messageElement, Object updatedContent);
    public void readUpdatedMessage();
    public void readDeletedMessage();

}
