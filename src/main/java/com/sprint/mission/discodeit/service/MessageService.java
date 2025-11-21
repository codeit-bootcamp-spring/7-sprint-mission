package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    public MessageReadResponseDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments);
    public List<MessageReadResponseDto> readAllMessage();
    public List<MessageReadResponseDto> findallByChannelId(UUID channelId);
    public void deleteMessage(UUID messageId);
    public MessageReadResponseDto patchMessage(MessagePatchRequestDto dto, UUID messageId);
    List<MessageReadResponseDto> readAllMessageByUserId(UUID userId);
    void resetMessage();
}
