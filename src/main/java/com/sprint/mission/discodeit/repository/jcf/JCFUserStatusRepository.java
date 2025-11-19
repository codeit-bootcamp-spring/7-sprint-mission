package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusStore = new ConcurrentHashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusStore.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusStore.values());
    }

    @Override
    public void update(UserStatus status) {
        userStatusStore.replace(status.getUserId(), status);
    }

    @Override
    public void deleteById(UUID id) {
        userStatusStore.remove(id);
    }
}
