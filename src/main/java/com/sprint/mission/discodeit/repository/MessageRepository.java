package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    List<Message> findAllByUserId(UUID userId);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findByContentContaining(String searchText);
    Message findById(UUID id);
    Message save(Message message);
    Message update(UUID id, String content);
    Message delete(UUID id);
}
