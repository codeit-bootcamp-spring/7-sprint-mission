package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageRequest request);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UpdateMessageRequest request);
    void delete(UUID messageId);
}
