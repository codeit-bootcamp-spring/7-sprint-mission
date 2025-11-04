package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponse create(CreateMessageRequest request);
    MessageResponse find(UUID messageId);
    List<MessageResponse> findAllByChannelId(FindAllByChannelIdMessageRequest channelId);
    MessageResponse update(UpdateMessageRequest request);
    void delete(DeleteMessageRequest messageId);
}
