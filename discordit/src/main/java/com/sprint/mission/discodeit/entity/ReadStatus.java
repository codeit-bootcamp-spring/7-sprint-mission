package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private final User user;
    private final Channel channel;
    @Setter
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }

    public void read() {
        this.lastReadAt = Instant.now();
        update();
    }

    public void read(Instant readAt) {
        this.lastReadAt = readAt;
        update();
    }

    public static ReadStatus fromDto(UUID id, User user, Channel channel,
                                     Instant createdAt, Instant updatedAt, Instant lastReadAt) {
        ReadStatus r = new ReadStatus(user, channel);
        r.lastReadAt = lastReadAt;
        r.uuid = id;
        r.createdAt = createdAt;
        r.updatedAt = updatedAt;
        return r;
    }

}
