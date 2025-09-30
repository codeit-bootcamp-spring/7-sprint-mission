package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelUser;
import com.sprint.mission.discodeit.repository.ChannelUserRepository;
import com.sprint.mission.discodeit.service.ChannelUserService;

import java.util.UUID;

public class JCFChannelUserService extends JCFBaseService<ChannelUser, UUID, ChannelUserRepository> implements ChannelUserService {
    private final ChannelUserRepository repository;
    protected JCFChannelUserService(ChannelUserRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
