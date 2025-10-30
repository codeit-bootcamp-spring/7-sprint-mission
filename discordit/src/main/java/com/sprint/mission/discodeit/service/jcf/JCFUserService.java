package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JCFUserService extends BasicUserService {

    public JCFUserService(UserRepository userRepository) {
        super(userRepository);
    }
}
