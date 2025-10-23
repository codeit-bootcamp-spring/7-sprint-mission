package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFUserRepository implements UserRepository {
    private final List<User> data = new ArrayList<>();

    @Override
    public User save(User user) {
        data.add(user);
        return user;
    }
}
