package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.messageDto.ChannelMessageCreateDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.DirectMessageCreateDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    MessageInfoDto createDirectMessage(DirectMessageCreateDto createDto); // 생성
    MessageInfoDto createChannelMessage(ChannelMessageCreateDto createDto);

    Optional<MessageInfoDto> findMessageById(UUID messageId);

    List<MessageInfoDto> findMessageBetweenUsers(UUID userId1, UUID userId2);     // 유저 둘의 메시지 전체 조회
    List<MessageInfoDto> findChannelMessage(UUID channelId);     // 한 채널의 메시지 전체 조회

    Optional<MessageInfoDto> updateMessage(UUID id, String newContent);      // 수정

    boolean deleteMessage(UUID id);              // 삭제
}
