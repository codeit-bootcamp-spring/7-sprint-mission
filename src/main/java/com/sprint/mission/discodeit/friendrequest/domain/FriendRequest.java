package com.sprint.mission.discodeit.friendrequest.domain;

import com.sprint.mission.discodeit.user.domain.User;
import lombok.Getter;


import java.io.Serializable;
import java.util.UUID;


@Getter
public class FriendRequest implements Serializable {
    private static final long serialVersionUID = 4L;

    private final UUID id;
    private final UUID senderId;
    private final UUID receiverId;
    private final Long createdAt;
    



    public FriendRequest(UUID senderId, UUID receiverId) {
        this.id= UUID.randomUUID();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt=System.currentTimeMillis();
    }


}
