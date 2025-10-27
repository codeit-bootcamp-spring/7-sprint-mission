package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.channels.Pipe;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Common {

  private  UUID userId;
    private  UUID channelId;

}
