package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    void remove(UUID messageId);

    Optional<Message> findById(UUID messageId);

    List<Message> findAll();
}
