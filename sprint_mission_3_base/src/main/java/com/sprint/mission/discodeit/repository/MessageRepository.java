package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
      SELECT m FROM Message m
      JOIN FETCH m.author
      WHERE m.channel.id = :channelId
        AND m.createdAt < :cursor
      """)
    Slice<Message> findAllByChannelIdWithAuthor(
            @Param("channelId") UUID channelId,
            @Param("cursor") Instant cursor,
            Pageable pageable
    );

    @Query("""
      SELECT MAX(m.createdAt)
      FROM Message m
      WHERE m.channel.id = :channelId
      """)
    Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

    @Query("""
      SELECT DISTINCT m FROM Message m
      JOIN FETCH m.author
      JOIN FETCH m.channel
      LEFT JOIN FETCH m.attachments
      WHERE m.id = :messageId
      """)
    Optional<Message> findDetailById(@Param("messageId") UUID messageId);
}