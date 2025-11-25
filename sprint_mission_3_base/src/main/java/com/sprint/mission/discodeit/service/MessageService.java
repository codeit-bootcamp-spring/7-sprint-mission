package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MessageService {

    MessageDto createWithFile(MessageCreateRequest request, MultipartFile file);

    MessageDto update(MessageUpdateRequest request);

    void delete(UUID messageId);

    MessageDto find(UUID id);

    PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page, int size);
}
