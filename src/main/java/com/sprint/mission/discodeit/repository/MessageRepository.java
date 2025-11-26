package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannelId(UUID channelId);

  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC LIMIT 1")
  Instant findLastMessageAtByChannelId(UUID channelId);

  void deleteByChannelId(UUID channelId);
}
