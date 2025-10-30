package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus{
    private UUID id;
    private User user;
    private Channel channel;
    private Instant lastReadAt;

    public void read() {
        this.lastReadAt = Instant.now();
    }

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }
}
