package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    public MessageDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments);
    public List<MessageDto> readAllMessage();
    public List<MessageDto> findallByChannelId(UUID channelId);
    public void deleteMessage(UUID messageId);
    public MessageDto patchMessage(MessagePatchRequestDto dto, UUID messageId);
    List<MessageDto> readAllMessageByUserId(UUID userId);
    void resetMessage();
}
