package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends BaseRepository<ReadStatus> {
    // 유저가 채널에서 얼마나 읽었는지 확인
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    // 채널의 모든 읽음 상태 제거
    void deleteAllByChannelId(UUID channelId);
    // 채널에서 한 유저의 읽음 상태 제거
    void deleteAllByUserIdAndChannelId(UUID userId, UUID channelId);

}
