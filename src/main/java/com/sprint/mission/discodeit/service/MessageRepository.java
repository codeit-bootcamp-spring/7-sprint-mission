package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.DeletedMessageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.DeletedMessage;
import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository {

    public MessageDto getMessageById(UUID messageId);
    public MessageDto getMessageByName(String messageName);
    public MessageDto getMessage(MessageDto messageDto);

    public void saveMessage(MessageDto messageDto);
    public void deleteMessage(MessageDto messageDto);
    public <T>void updateMessage(MessageDto messageDto, Message.messageElement messageElement, T updatedContent);
    public MessageDto[] getUpdatedMessage();
    public DeletedMessageDto[] getDeletedMessage();
    public MessageDto[] getAllMessage();
    public void setDefaultSender(MessageDto messageDto);
    public void resetMessageRepository();

}
