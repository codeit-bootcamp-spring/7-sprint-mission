package com.sprint.mission.discodeit.entity.status.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends BaseRepository<ReadStatus> {
    // 유저가 채널에서 얼마나 읽었는지 확인
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    // 유저의 모든 채널의 읽음 상태
    List<ReadStatus> findAllByUserId(UUID userId);

    // 채널의 모든 읽음 상태 제거
    void deleteAllByChannelId(UUID channelId);

    // 채널에서 한 유저의 읽음 상태 제거
    void deleteAllByUserIdAndChannelId(UUID userId, UUID channelId);

}
