package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.DirectMessage;

import java.util.UUID;

public interface DirectMessageService extends BaseService<DirectMessage, UUID> {
    DirectMessage create(UUID receiverId, UUID senderId, String message);
    void read(UUID messageId);

}
