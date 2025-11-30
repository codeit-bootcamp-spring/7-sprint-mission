package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(CreateMessageRequest request, List<BinaryContentCreateRequest> binaryContentCreateRequests);

    MessageDto find(UUID messageId);

    Slice<MessageDto> findAllByChannelId(UUID channelId, int page);

    MessageDto update(UUID messageId, UpdateMessageRequest request);

    void delete(UUID messageId);
}
