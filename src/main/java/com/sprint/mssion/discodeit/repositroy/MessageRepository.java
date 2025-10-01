package com.sprint.mssion.discodeit.repositroy;

import com.sprint.mssion.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    Optional<Message> findById(UUID messageId);

    List<Message> findAll();

    void deleteById(UUID messageId);

    boolean existsById(UUID messageId);
}
