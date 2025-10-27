package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.*;

public class JCFUserService extends BasicUserService {

    public JCFUserService(UserRepository userRepository) {
        super(userRepository);
    }
}
