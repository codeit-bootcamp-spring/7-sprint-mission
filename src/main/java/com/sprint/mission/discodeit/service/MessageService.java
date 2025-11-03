package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.messageDto.ChannelMessageRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.DirectMessageRequestDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageResponseDto;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto createDirectMessage(DirectMessageRequestDto requestDto); // 생성
    MessageResponseDto createChannelMessage(ChannelMessageRequestDto requestDto);

    Optional<MessageResponseDto> findMessageById(UUID messageId);

    List<MessageResponseDto> findMessageBetweenUsers(UUID userId1, UUID userId2);     // 유저 둘의 메시지 전체 조회
    List<MessageResponseDto> findAllByChannelId(UUID channelId);     // 한 채널의 메시지 전체 조회

    Optional<MessageResponseDto> updateMessage(MessageUpdateDto updateDto);      // 수정

    boolean deleteMessage(UUID id);              // 삭제
}
