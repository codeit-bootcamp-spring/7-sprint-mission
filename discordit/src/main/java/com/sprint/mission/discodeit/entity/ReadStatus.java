package com.sprint.mission.discodeit.entity;

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

    public static ReadStatus fromDto(UUID id, User user, Channel channel, Instant lastReadAt) {
        ReadStatus r = new ReadStatus(user, channel);
        r.id = id;
        r.lastReadAt = lastReadAt;
        return r;
    }
}
