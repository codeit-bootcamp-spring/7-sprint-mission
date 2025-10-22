package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(UUID senderId, UUID receiverId, String content) {
        Message newMessage = new Message(senderId, receiverId, content);
        return messageRepository.save(newMessage);
    }

    @Override
    public Message findMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message byId = messageRepository.findById(id);
        byId.setContent(content);
        return messageRepository.save(byId);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.delete(id);
    }
}
