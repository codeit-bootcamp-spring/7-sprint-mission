package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public JCFMessageService() {
        this.messageRepository = new JCFMessageRepository();
    }

    @Override
    public Message create(UUID userId, UUID channelId, String content) {
        Message m = new Message(userId, channelId, content);
        messageRepository.save(m);
        return m;
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(UUID id, String content) {
        Message m = messageRepository.findById(id);
        if (m != null) {
            m.update(content);
            messageRepository.save(m);
        }
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}