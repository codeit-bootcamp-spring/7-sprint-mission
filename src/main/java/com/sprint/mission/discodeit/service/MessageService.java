package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
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
    Message create(CreateMessageRequest request,List<BinaryContentCreateRequest> binaryContentCreateRequests);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId,UpdateMessageRequest request);
    void delete(UUID messageId);
}
