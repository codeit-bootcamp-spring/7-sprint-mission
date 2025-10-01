package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class ChannelInviteRequest {

    private UUID channelId;
    private UUID userId;

    public ChannelInviteRequest(Channel channel, UUID userId){
        this.channelId=channel.getId();
        this.userId=userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }
}
