package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessagePageRepository extends JpaRepository<Message , UUID> {

    Page<Message> findByChannelId(UUID channelId, Pageable pageable);
}
