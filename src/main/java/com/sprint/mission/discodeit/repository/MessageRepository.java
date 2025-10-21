package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID uuid);
    List<Message> findAll();
    boolean deleteById(UUID uuid);
    List<Message> findByChannel(UUID channelId);
    List<Message> findByAuthor(UUID authorId);
    List<Message> findByChannelIdAndAuthorId(UUID channelId, UUID authorId);
    List<Message> searchByKeyword(String keyword);
}
