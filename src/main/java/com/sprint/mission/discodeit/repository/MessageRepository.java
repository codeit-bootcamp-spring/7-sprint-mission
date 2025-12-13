package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    void deleteById(UUID id);
}
