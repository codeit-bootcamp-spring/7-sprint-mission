package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class FileUserService extends BasicUserService {

    public FileUserService(UserRepository userRepository) {
        super(userRepository);
    }
}
