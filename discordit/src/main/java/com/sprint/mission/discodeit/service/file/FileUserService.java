package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;

public class FileUserService extends BasicUserService {

    public FileUserService(UserRepository userRepository) {
        super(userRepository);
    }
}
