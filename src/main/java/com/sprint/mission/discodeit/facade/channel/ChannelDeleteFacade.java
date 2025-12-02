package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.service.ChannelMemberService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelDeleteFacade {

  private final ChannelService channelService;
  private final MessageService messageService;
  private final ChannelMemberService channelMemberService;
  private final BinaryContentStorage binaryContentStorage;

  public void deleteChannel(@NonNull UUID channelId) {
    channelMemberService.findAllByChannelId(channelId)
        .forEach(readStatus -> channelMemberService.delete(readStatus.getId()));
    messageService.findAllByChannelId(channelId).forEach(message -> {
      message.getAttachments().forEach(attachment ->
          binaryContentStorage.delete(attachment.getId()));
      messageService.delete(message.getId());
    });
    channelService.delete(channelId);
  }
}
