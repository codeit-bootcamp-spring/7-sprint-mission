package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ChannerlMemberService;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChannelDeleteFacade {

  private final ChannelService channelService;
  private final MessageService messageService;
  private final ChannerlMemberService channerlMemberService;

  @Transactional
  public void deleteChannel(@NonNull UUID channelId) {
    channerlMemberService.findAllByChannelId(channelId)
        .forEach(readStatus -> channerlMemberService.delete(readStatus.getId()));
    messageService.findAllByChannelId(channelId)
        .forEach(message -> messageService.delete(message.getId()));
    channelService.delete(channelId);
  }
}
