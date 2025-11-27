package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // 한 채널의 모든 대화 목록
    // 오프셋기반 페이징
//    @Query("SELECT m FROM Message m JOIN FETCH m.author WHERE m.channel = ?1")
//    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
    // 커서 기반 페이징
    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.author " +
            "WHERE m.channel.id = ?1 " +
            "AND m.createdAt < ?2 " +
            "ORDER BY m.createdAt DESC")
    List<Message> findAllByChannelId(UUID channelId, Instant cursorAt, Pageable pageable);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
