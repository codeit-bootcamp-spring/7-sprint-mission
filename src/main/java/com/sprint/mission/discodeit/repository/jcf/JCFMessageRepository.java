package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID,Message> messageRepo ;


    public JCFMessageRepository() {
        this.messageRepo = new HashMap<>();

        resetMessageRepository();
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(messageRepo.get(messageId));
    }

    @Override
    public Optional<Message> getMessageByName(String messageName) {
        return messageRepo.values().stream().filter(x->x.getContent().equals(messageName)).findFirst();
    }

    @Override
    public Optional<Message> getMessage(Message message) {
        return getMessageById(message.getId());
    }

    @Override
    public Message saveMessage(Message message) {
        messageRepo.put(message.getId(),message);
        return message;
    }

    @Override
    public void deleteMessage(Message message) {
       messageRepo.remove(message.getId());
    }

    @Override
    public void updateMessage(Message message) {
        deleteMessage(message);
        saveMessage(message);
    }

    @Override
    public List<Message> getUpdatedMessage() {
        return messageRepo.values().stream().filter(x->x.getUpdatedAt()!= x.getCreatedAt()).toList();
    }



    @Override
    public List<Message> getAllMessage() {
        return messageRepo.values().stream().toList();
    }



    @Override
    public void resetMessageRepository() {
        messageRepo.clear();

    }


}
