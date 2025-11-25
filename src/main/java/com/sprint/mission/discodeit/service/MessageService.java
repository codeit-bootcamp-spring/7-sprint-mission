package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDtoBasic;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageDto createMessage(MessageCreateRequestDto messageCreateRequestDto
            , List<MultipartFile> attachments) throws IOException;
     List<MessageDto> readAllMessage();
     PageResponseDtoBasic<MessageDto> findallByChannelId(UUID channelId, Pageable pageable);
    PageResponseDto<MessageDto> findallByChannelIdWithCursor(UUID channelId, String cursor, Pageable pageable);
    void deleteMessage(UUID messageId);
    MessageDto patchMessage(MessagePatchRequestDto dto, UUID messageId);
    List<MessageDto> readAllMessageByUserId(UUID userId);
    void resetMessage();
}
