package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface DirectMessageRepository extends BaseRepository<DirectMessage, UUID> {

    /**
     * 특정 사용자가 받은 모든 메시지를 조회합니다. (삭제되지 않은 메시지만)
     * @param receiverId 받는 사용자의 ID
     * @return 해당 사용자가 받은 메시지 목록
     */
    List<DirectMessage> findByReceiverId(UUID receiverId);

    /**
     * 특정 사용자가 보낸 모든 메시지를 조회합니다. (삭제되지 않은 메시지만)
     * @param senderId 보내는 사용자의 ID
     * @return 해당 사용자가 보낸 메시지 목록
     */
    List<DirectMessage> findBySenderId(UUID senderId);

    /**
     * 두 사용자 간에 주고받은 모든 메시지를 조회합니다. (삭제되지 않은 메시지만)
     * @param userOneId 첫 번째 사용자 ID
     * @param userTwoId 두 번째 사용자 ID
     * @return 두 사용자 간의 모든 메시지 목록
     */
    List<DirectMessage> findByParticipants(UUID userOneId, UUID userTwoId);

    /**
     * 사용자가 보낸 모든 메시지를 삭제합니다.
     * @param senderId 첫 번째 사용자 ID
     */
    void deleteAllBySenderId(UUID senderId);
}