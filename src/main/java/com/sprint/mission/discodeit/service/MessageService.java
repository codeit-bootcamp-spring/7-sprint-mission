package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageService
 * -----------------
 * 메시지 전송/조회/수정/삭제 등의 비즈니스 로직을 정의한 인터페이스입니다.
 */
public interface MessageService {

    /** 새로운 메시지를 생성 */
    void createMessage(CreateMessageRequestDto request);

    /** 두 유저 또는 채널 간의 최신 메시지(가장 마지막 메시지)를 가져옴 */
    Message getLastestMessage(UUID senderId, UUID receiverId, ReceiveType receiverType);

    /** 두 유저 간의 전체 메시지 목록을 조회 */
    List<Message> getMessagesBetween(UUID userId1, UUID userId2);

    /** 특정 채널에 포함된 모든 메시지 조회 */
    List<Message> getAllByChannel(UUID channelId);

    /** 유저가 특정 유저 또는 채널에게 보낸 모든 메시지 조회 */
    List<Message> getAllSentByUser(UUID senderId, UUID receiverId);

    /** 메시지 내용(content)을 수정 */
    void updateMessage(UpdateMessageRequestDto request);

    /** 특정 메시지를 UUID로 삭제 */
    void deleteMessage(UUID id);
}
