package com.sprint.mission.discodeit.repository;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message create(User sender, User receiver, String message);
    Message read(UUID messageId);
    List<Message> readAll();
    Message update(UUID messageId, String content);
    boolean delete(UUID messageId);
}
