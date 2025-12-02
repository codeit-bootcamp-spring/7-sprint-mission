package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageFactory {

  private final UserService userService;
  private final ChannelService channelService;

  public Message create(UUID speakerId, UUID channelId, MessageCreateReq req,
      List<BinaryContent> attachments) {

    User speaker = userService.findById(speakerId);
    Channel channel = channelService.findById(channelId);
    return Message.create(
        channel,
        speaker,
        req.content(),
        attachments
    );
  }
}