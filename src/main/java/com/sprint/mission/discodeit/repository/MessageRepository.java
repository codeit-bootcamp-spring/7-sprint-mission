package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannelId(UUID channelId);

  @Query("""
        SELECT MAX(m.createdAt)
        FROM Message m
        WHERE m.channel.id = :channelId
      """)
  Optional<Instant> findLatestCreatedAt(@Param("channelId") UUID channelId);

  // 특정 채널 메시지 최신순 50개 가져오기
  Slice<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
}
