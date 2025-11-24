package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageDto createMessage(MessageCreateRequest requestDto, List<MultipartFile> attachments);

    // findAll
    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);     // 한 채널의 메시지 전체 조회

    MessageDto updateMessage(UUID messageId, MessageUpdateRequest updateDto);      // 수정

    void deleteMessage(UUID id);              // 삭제
}
