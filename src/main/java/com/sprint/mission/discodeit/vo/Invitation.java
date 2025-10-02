package com.sprint.mission.discodeit.vo;

import lombok.Getter;


import java.util.UUID;


@Getter
public class Invitation {

    private final UUID senderId;
    private final UUID receiverId;
    private final InvitationType type;


    public Invitation(UUID senderId, UUID receiverId, InvitationType type) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type=type;
    }
}
