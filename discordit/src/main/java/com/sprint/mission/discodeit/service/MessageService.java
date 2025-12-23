package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.channel.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.entity.message.MessageDto;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void remove(UUID id);

    MessageDto editMessage(UUID id, MessageEditRequest request);

    List<MessageDto> getAll();

    PageResponse<Message> getAllByChannelId(MessageGetRequest request);

    MessageDto send(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments);
}
