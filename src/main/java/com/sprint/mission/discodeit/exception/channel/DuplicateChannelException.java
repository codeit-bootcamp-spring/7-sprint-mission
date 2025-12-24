package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateChannelException extends ChannelException {

  public DuplicateChannelException(String channelName) {
    super(ErrorCode.DUPLICATE_CHANNEL,
        Map.of("channelName", channelName));
  }

}
