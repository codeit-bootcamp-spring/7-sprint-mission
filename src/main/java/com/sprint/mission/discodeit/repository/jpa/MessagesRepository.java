package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessagesRepository extends JpaRepository<Message, UUID> {
    Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);
    Slice<Message> findByChannelId(UUID channelId, Pageable pageable);
}
