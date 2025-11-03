package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class ChannelMember implements Serializable {

    private final UUID memberId;
    private final ReadStatus readStatus;

    public ChannelMember(UUID memberId) {
        this.memberId = memberId;
        this.readStatus =  new ReadStatus();
    }
}
