package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    // 유저가 채널에서 얼마나 읽었는지 확인
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    // 유저의 모든 채널의 읽음 상태
    List<ReadStatus> findAllByUserId(UUID userId);

    // 채널로 수신 상태 찾기
    List<ReadStatus> findAllByChannelId(UUID channelId);

    // 채널의 모든 읽음 상태 제거
    void deleteAllByChannelId(UUID channelId);

    // 채널에서 한 유저의 읽음 상태 제거
    void deleteByUserIdAndChannelId(UUID userId, UUID channelId);
}
