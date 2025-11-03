package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends BaseRepository<Message>{

    // 한 채널의 모든 대화 목록
    List<Message> findAllByChannelId(UUID channelId);
    // 두 유저 간의 모든 대화 목록
    List<Message> findAllByBetweenUserIds(UUID userId1, UUID userId2);

    // 채널이 삭제되면 메시지도 삭제
    void deleteAllByChannelId(UUID channelId);

    Optional<Message> findTopByChannelId(UUID channelId);
}
