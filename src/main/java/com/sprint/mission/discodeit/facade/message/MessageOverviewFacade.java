package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.facade.mapper.MessageFacadeMapper;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageOverviewFacade {

  private final MessageService messageService;
  private final MessageFacadeMapper messageFacadeMapper;

  //메세지 전체 조회
  public List<MessageViewRes> findAllByChannelId(@NonNull UUID channelId) {
    return messageService.findAllByChannelId(channelId).stream()
        .map(messageFacadeMapper::mapToView).toList();
  }
}
