package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message  create(User sender, User receiver, String message);
    Message read(UUID messageId);
    List<Message> readAll();
    Message update(UUID messageId, String content);
    boolean delete(UUID messageId);
}
