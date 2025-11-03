/*
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final BasicMessageService basicMessageService;

    public JCFMessageService(MessageRepository messageRepository,  ChannelRepository channelRepository,  UserRepository userRepository) {
        this.basicMessageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
    }

    @Override public Message create(Message message) {return basicMessageService.create(message);}
    @Override public Message get(UUID uuid) {return basicMessageService.get(uuid);}
    @Override public List<Message> getAll() {return basicMessageService.getAll();}
    @Override public Message update(Message message) {return basicMessageService.update(message);}
    @Override public boolean delete(UUID uuid) {return basicMessageService.delete(uuid);}
    // 특정 채널별 메세지 조회
    @Override public List<Message> getMessagesByChannel(UUID channelId) {return basicMessageService.getMessagesByChannel(channelId);}
    // 특정 작성자별 메세지 조회
    @Override public List<Message> getMessagesByAuthor(UUID authorId) {return basicMessageService.getMessagesByAuthor(authorId);}
    // 특정 채널의 특정 작성자 메세지 조회
    @Override public List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {return basicMessageService.getMessagesByChannelAndAuthor(channelId, authorId);}
    // 모든 채널의 메세지 조회
    @Override public List<Message> getAllMessages() {return basicMessageService.getAllMessages();}
    // 특정 키워드 검색
    @Override public List<Message> searchByKeyword(String keyword) {return basicMessageService.searchByKeyword(keyword);}
}

 */
