package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.nio.channels.Pipe;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatus extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

  private  UUID userId;
    private  UUID channelId;




    public void update() {
        this.setUpdatedAt(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
    }
}

