package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByChannelId(UUID channelId);
}
