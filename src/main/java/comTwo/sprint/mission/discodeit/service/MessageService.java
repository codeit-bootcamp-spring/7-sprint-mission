package com2.sprint.mission.discodeit.service;

import com2.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(Message message);
    Message read(UUID id);
    List<Message> readAll();
    Message update(UUID id, Message message);
    boolean delete(UUID id);
}