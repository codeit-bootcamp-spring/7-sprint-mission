package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    public Optional<Message> getMessageById(UUID messageId);
    public Optional <Message> getMessageByName(String messageName);
    public Optional <Message> getMessage(Message message);

    public Message saveMessage(Message message);
    public void deleteMessage(Message message);
    public <T>void updateMessage(Message message);
    public List<Message> getUpdatedMessage();
//    public DeletedMessage[] getDeletedMessage();
    public List<Message> getAllMessage();
//    public void setDefaultSender(Message message);
    public void resetMessageRepository();

}
