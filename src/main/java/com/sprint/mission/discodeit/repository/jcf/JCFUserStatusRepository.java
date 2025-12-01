/*
package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        Objects.requireNonNull(userStatus);
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID Id) {
        return Optional.ofNullable(data.get(Objects.requireNonNull(Id)));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Objects.requireNonNull(userId);
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return data.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId));
    }

    @Override
    public boolean deleteById(UUID id) {
        return data.remove(Objects.requireNonNull(id)) != null;
    }
}

 */
