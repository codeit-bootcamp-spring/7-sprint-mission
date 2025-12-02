package com.sprint.mission.discodeit.dto.channel.query;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelInfoQuery {

  private UUID channelId;
  private String name;
  private String description;
  private ChannelType publicType;
  private UUID managerId;
  private Instant lastMessageTime;
}
