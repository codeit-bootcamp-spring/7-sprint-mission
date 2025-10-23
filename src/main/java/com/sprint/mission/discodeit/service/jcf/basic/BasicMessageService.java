package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    //의존성 주입이긴한데 이거 일커지면 더 늘어나는데
    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;

    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        //둘의 uuid가 존재유무판단
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널UUID가없어 :" + channelId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("매시지UUID가 없어 :" + authorId);
        }

        Message message = new Message( channelId, authorId,content);
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return  messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + messageId));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteById(messageId);
    }
}
