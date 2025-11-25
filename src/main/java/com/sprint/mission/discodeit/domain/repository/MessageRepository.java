package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.Message;

import java.util.List;
import java.util.Optional;


public interface MessageRepository {
    void save(Message message);

    void delete(Message message);

    Optional<Message> findById(String id);

    List<Message> findAll();

    List<Message> findByChannelId(String channelId);
}
