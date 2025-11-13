package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.messageDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    Message createMessage(MessageRequestDto requestDto, List<MultipartFile> attachments);

    // findAll
    List<Message> findAllByChannelId(UUID channelId);     // 한 채널의 메시지 전체 조회

    Message updateMessage(UUID messageId, MessageUpdateDto updateDto);      // 수정

    void deleteMessage(UUID id);              // 삭제
}
