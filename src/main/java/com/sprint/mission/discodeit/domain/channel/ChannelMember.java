package com.sprint.mission.discodeit.domain.channel;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ChannelMember implements Serializable {

    private final UUID userId;
    private final String NickName;
    private final ReadStatus readStatus;

    public ChannelMember(UUID userId, String nickName) {
        this.userId = userId;
        this.NickName = nickName;
        this.readStatus =  new ReadStatus();
    }

    public void readMessage(){

    }
}
