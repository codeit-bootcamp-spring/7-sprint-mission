package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface MessageService {
    Message create(Message message);
    Message get(UUID uuid);
    List<Message> getAll();
    Message update(Message message);
    boolean delete(UUID uuid);
    List<Message> getMessagesByChannel(UUID channelId);
    List<Message> getMessagesByAuthor(UUID authorId);
    List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId);
    List<Message> getAllMessages();
    List<Message> searchByKeyword(String keyword);
}
