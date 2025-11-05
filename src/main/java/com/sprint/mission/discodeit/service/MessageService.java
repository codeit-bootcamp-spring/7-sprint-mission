package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface MessageService {
    Message create(MessageCreateRequestDto messageCreateRequestDto);
    Message get(UUID uuid);
    List<Message> getAll();
    List<Message> getAllByChannelId(UUID channelId);
    List<Message> getAllByChannelForUser(UUID channelId, UUID userId);
    List<Message> getAllByUserId(UUID userId);
    Message update(MessageUpdateRequestDto messageUpdateRequestDto);
    boolean delete(UUID uuid);
    List<Message> getMessagesByAuthor(UUID authorId);
    List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId);
    List<Message> searchByKeyword(String keyword);
}
