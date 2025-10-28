package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    /** (userId, channelId) 조합은 1개만 존재해야 함 */
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    //저장또는 수정
  //  ReadStatus save(ReadStatus status);

    /** 한 유저의 모든 채널 읽음 상태 보기 */
    List<ReadStatus> findAllByUserId(UUID userId);

    /** 필요 시 정리/삭제 */
    void deleteById(UUID id);

    void save(UUID bose, UUID id);
}
