package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.DirectMessage;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.DirectMessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.DirectMessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class JCFDirectMessageService extends JCFBaseService<DirectMessage, UUID, DirectMessageRepository> implements DirectMessageService {
    private final DirectMessageRepository repository;

    public JCFDirectMessageService(DirectMessageRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public DirectMessage create(UUID receiverId, UUID senderId, String message) {
        return null;
    }

    @Override
    public void read(UUID messageId) {

    }
}