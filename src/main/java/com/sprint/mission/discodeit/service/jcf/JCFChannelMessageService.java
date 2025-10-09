package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.repository.ChannelMessageRepository;
import com.sprint.mission.discodeit.service.ChannelMessageService;

import java.util.UUID;

public class JCFChannelMessageService extends JCFBaseService<ChannelMessage, UUID, ChannelMessageRepository> implements ChannelMessageService {
    protected JCFChannelMessageService(ChannelMessageRepository repository) {
        super(repository);
    }
}
