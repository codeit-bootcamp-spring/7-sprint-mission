package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

/**
 * MessageService
 * -----------------
 * 메시지 전송/조회/수정/삭제 등의 비즈니스 로직을 정의한 인터페이스입니다.
 */
public interface MessageService {

    /** 새로운 메시지를 생성 */
    MessageResponseDto create(CreateMessageRequestDto messageRequest, List<CreateBinaryContentRequestDto> binaryContentRequests);

    MessageResponseDto find(UUID messageId);

    /** 특정 채널에 포함된 모든 메시지 조회 */
    Slice<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable);

    /** 메시지 내용(content)을 수정 */
    MessageResponseDto update(UUID messageId, UpdateMessageRequestDto request);

    /** 특정 메시지를 UUID로 삭제 */
    void delete(UUID messageId);
}
