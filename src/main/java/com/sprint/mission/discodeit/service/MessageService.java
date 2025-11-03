package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface MessageService {
    MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto);
    MessageResponseDto get(UUID uuid);
    List<MessageResponseDto> getAll();
    List<MessageResponseDto> getAllByChannelId(UUID channelId);
    MessageResponseDto update(MessageUpdateRequestDto messageUpdateRequestDto);
    boolean delete(UUID uuid);
    List<Message> getMessagesByChannel(UUID channelId);
    List<Message> getMessagesByAuthor(UUID authorId);
    List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId);
    List<Message> getAllMessages();
    List<Message> searchByKeyword(String keyword);
}
