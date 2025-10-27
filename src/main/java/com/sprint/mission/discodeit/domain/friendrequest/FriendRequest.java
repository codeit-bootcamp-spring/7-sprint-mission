package com.sprint.mission.discodeit.domain.friendrequest;

import com.sprint.mission.discodeit.domain.user.exception.CannotSendFriendRequestToSelfException;
import lombok.Getter;


import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class FriendRequest implements Serializable {
    private static final long serialVersionUID = 4L;

    private final UUID id;
    private final UUID senderId;
    private final UUID receiverId;
    private final Instant createdAt;


    public FriendRequest(UUID senderId, UUID receiverId) {
        validateNotSelf(senderId, receiverId);
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = Instant.now();
    }

    private static void validateNotSelf(UUID senderId, UUID receiverId) {
        if (senderId.equals(receiverId)) {
            throw new CannotSendFriendRequestToSelfException();
        }
    }



}
