package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User read(UUID id) {
        return null;
    }

    @Override
    public List<User> readAll() {
        return List.of();
    }

    @Override
    public User update(UUID id, User user) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
