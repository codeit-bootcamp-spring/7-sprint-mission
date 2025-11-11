package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDto createMessage(CreateMessageDto createMessageDto, List<CreateBinaryContentDto> createBinaryContentDtos);

    MessageResponseDto getMessage(UUID messageId);

    List<MessageResponseDto> getAllMessages();

    List<MessageResponseDto> getAllMessageByChannelId(UUID channelID);

    MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto);

    void deleteMessage(UUID messageId);
}
