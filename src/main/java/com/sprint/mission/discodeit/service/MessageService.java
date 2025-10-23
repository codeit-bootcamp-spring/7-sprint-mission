package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    MessageInfo createDirectMessage(UUID authorId, UUID receiverId, String content); // 생성
    MessageInfo createChannelMessage(UUID authorId, UUID channelId, String content);

    Optional<MessageInfo> findMessageById(UUID messageId);

    List<MessageInfo> findMessageBetweenUsers(UUID userId1, UUID userId2);     // 유저 둘의 메시지 전체 조회
    List<MessageInfo> findChannelMessage(UUID channelId);     // 한 채널의 메시지 전체 조회

    Optional<MessageInfo> updateMessage(UUID id, String newContent);      // 수정

    boolean deleteMessage(UUID id);              // 삭제
}
