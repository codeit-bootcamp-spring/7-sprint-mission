package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.MessageGetByChannelIdRequest;
import com.sprint.mission.discodeit.dto.message.request.*;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseV2;
import com.sprint.mission.discodeit.entity.Message;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageResponseV2> get(MessageGetRequest dto);
    void remove(UUID message);
    /**
     * 테스트용 임시 메서드: 마지막으로 전송된 메시지를 반환합니다.
     */
    Message getLastMessage();

    MessageResponseV2 editMessage(UUID id, MessageEditRequest request);

    List<MessageResponseV2> getAll();

    List<MessageResponseV2> getAllByChannelId(@Valid MessageGetByChannelIdRequest request);

    MessageResponseV2 send(@Valid MessageCreateRequestV2 messageCreateRequest, List<MultipartFile> attachments);
}
