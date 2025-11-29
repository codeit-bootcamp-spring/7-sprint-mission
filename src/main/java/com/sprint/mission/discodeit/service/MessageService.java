package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageService {

  MessageResponseDto createMessage(CreateMessageRequestDto request,
      List<CreateBinaryContentRequestDto> fileRequest); // 메시지 생성

  PageResponse<MessageResponseDto> findAllByChannelId(UUID channelId,
      Pageable pageable); // 특정 채널 메시지 조회

  MessageResponseDto updateMessage(UUID messageId,
      UpdateMessageDto MessageUpdateRequest); //메시지 수정(업데이트)

  void deleteMessage(UUID messageId); // 메시지 삭제
}
