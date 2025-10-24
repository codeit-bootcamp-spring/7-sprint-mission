package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface DirectMessageService extends BaseService<DirectMessage, UUID> {

    /**
     * 새로운 1:1 메시지를 전송합니다.
     *
     * @param senderId   메시지를 보내는 사용자의 ID
     * @param receiverId 메시지를 받는 사용자의 ID
     * @param message    전송할 메시지 내용
     * @return 전송 및 저장된 DirectMessage 객체
     */
    DirectMessage sendMessage(UUID senderId, UUID receiverId, String message);

    /**
     * 특정 사용자가 받은 모든 메시지를 시간 순으로 조회합니다.
     *
     * @param receiverId 조회할 사용자의 ID
     * @return 받은 메시지 목록
     */
    List<DirectMessage> getMessagesByReceiver(UUID receiverId);

    /**
     * 특정 사용자가 보낸 모든 메시지를 시간 순으로 조회합니다.
     *
     * @param senderId 조회할 사용자의 ID
     * @return 보낸 메시지 목록
     */
    List<DirectMessage> getMessagesBySender(UUID senderId);

    /**
     * 두 사용자 간에 주고받은 모든 대화 내용을 시간 순으로 조회합니다.
     *
     * @param userOneId 첫 번째 사용자 ID
     * @param userTwoId 두 번째 사용자 ID
     * @return 두 사용자 간의 대화 목록
     */
    List<DirectMessage> getConversation(UUID userOneId, UUID userTwoId);
}