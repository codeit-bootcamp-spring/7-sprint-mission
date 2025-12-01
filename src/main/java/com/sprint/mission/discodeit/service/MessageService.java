package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageResponseDto createMessage(CreateMessageDto createMessageDto,
      List<CreateBinaryContentDto> createBinaryContentDtos);

  MessageResponseDto getMessage(UUID messageId);

  List<MessageResponseDto> getAllMessages();

  PageResponse getAllMessageByChannelId(UUID channelID, Instant cursor, Pageable page);

  MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto);

  void deleteMessage(UUID messageId);
}
