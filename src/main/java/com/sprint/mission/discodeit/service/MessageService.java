package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message insert(Channel channel, String message);
    List<Message> findAll();
    Message findById(UUID id);
    Message update(UUID id, String message);
    Message delete(UUID id);
}
