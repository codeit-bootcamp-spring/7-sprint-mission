package com.sprint.mission.discodeit.entity.base;

import java.util.UUID;

public class ReadStatus extends BaseEntity {
    public UUID id;
    public User user;
    public Channel channel;


    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }
}
