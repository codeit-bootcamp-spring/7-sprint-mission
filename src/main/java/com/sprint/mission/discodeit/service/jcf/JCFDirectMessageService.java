package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.DirectMessage;
import com.sprint.mission.discodeit.repository.DirectMessageRepository;
import com.sprint.mission.discodeit.service.DirectMessageService;

import java.util.UUID;

public class JCFDirectMessageService extends JCFBaseService<DirectMessage, UUID, DirectMessageRepository> implements DirectMessageService {
    protected JCFDirectMessageService(DirectMessageRepository repository) {
        super(repository);
    }
}
