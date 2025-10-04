package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.MessageInfo;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;
    public JCFMessageService() {this.data = new HashMap<>();}

    @Override
    public Message createDirectMessage(UUID authorId, UUID receiverId, String content) {
        return null;
    }

    @Override
    public Message createChannelMessage(UUID authorId, UUID channelId, String content) {
        return null;
    }

    @Override
    public Optional<MessageInfo> findMessageById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public List<MessageInfo> findDMBetweenUsers(UUID userId1, UUID userId2) {
        return List.of();
    }

    @Override
    public List<Message> findCMByChannel(UUID userId, UUID channelId) {
        return List.of();
    }

    @Override
    public Optional<MessageInfo> update(UUID id, String newContent) {
        return Optional.empty();
    }

    @Override
    public boolean deleteMessage(UUID id) {
        return false;
    }
}
