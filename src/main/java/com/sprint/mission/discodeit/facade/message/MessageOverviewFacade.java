package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.common.response.PageResponse;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.mapper.MessageFacadeMapper;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageOverviewFacade {

  private final MessageService messageService;
  private final MessageFacadeMapper messageFacadeMapper;

  //메세지 전체 조회
  public PageResponse<MessageViewRes> findAllByChannelId(@NonNull UUID channelId, int page) {
    Slice<Message> slice = messageService.getRecentMessages(channelId, page);

    // Entity -> DTO 변환 후 PageResponse 생성
    return PageResponseMapper.fromSlice(
        slice.map(messageFacadeMapper::mapToView) // Slice<T>에도 map 가능
    );
  }
}
