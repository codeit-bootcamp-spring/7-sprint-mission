package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    List<Message> findAll();
    List<Message> findAllByUserId(UUID userId);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findByContentContaining(String searchText);
    Optional<Message> findById(UUID id);
    Message save(Message message);
    void update(UUID id, String content, List<UUID> attachmentIds);
    void delete(UUID id);
    Optional<Message> findLastMessageByChannelId(UUID channelId);
    boolean existsById(UUID id);
}
