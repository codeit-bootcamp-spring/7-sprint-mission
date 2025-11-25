package com.sprint.mission.discodeit.repository.jpainterface;

import com.sprint.mission.discodeit.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaMessageRepository extends JpaRepository<MessageEntity, String> {
    List<MessageEntity> findAllByChannelId(UUID channelId);
}
