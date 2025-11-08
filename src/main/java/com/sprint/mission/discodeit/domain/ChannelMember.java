package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelMember implements Serializable {

    private final UUID memberId;
    private Instant readStatus;

    public ChannelMember(UUID memberId) {
        this.memberId = memberId;
        this.readStatus=Instant.now();
    }

    public void read(){
        this.readStatus=Instant.now();
    }
}
