package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(User user) {
        if (data.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " already exists.");
        }
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        return List.copyOf(data.values());
    }

    @Override
    public Optional<User> update(UUID id, User updatedUser) {
        return read(id).map(existingUser -> {
            // update 메소드를 사용하여 필드를 수정하고 updatedAt을 갱신합니다.
            existingUser.update(updatedUser.getUsername(), updatedUser.getEmail());
            return existingUser;
        });
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}

// JCFChannelService.java와 JCFMessageService.java도 유사한 구조로 작성합니다.