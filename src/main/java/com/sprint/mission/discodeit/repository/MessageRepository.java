package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageRepository
 * -----------------
 * 메시지 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Optional<Message> findTop1ByChannelIdOrderByCreatedAtDesc(UUID channelId);

    List<Message> findAllByChannelId(UUID channelId);


    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    Slice<Message> findAllByChannelIdAndCreatedAtBefore(UUID channelId, Instant cursor, Pageable pageable);

    void deleteAllByChannelId(UUID channelId);
}
