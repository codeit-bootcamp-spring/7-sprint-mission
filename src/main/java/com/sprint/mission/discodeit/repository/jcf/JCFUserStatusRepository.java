package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatuses = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID uuid) {
        return Optional.ofNullable(userStatuses.get(uuid));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuses.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatuses.values()
                .removeIf(status -> status.getUserId().equals(userId));
    }

    @Override
    public void delete(UUID uuid) {
        userStatuses.remove(uuid);
    }
}
