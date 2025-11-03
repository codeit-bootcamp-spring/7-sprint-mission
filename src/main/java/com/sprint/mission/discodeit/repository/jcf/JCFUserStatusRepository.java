package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatuses = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuses.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatuses.get(userStatusId));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }

    @Override
    public void deleteById(UUID userStatusId) {
        userStatuses.remove(userStatusId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatuses.containsKey(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatuses.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return userStatuses.containsKey(userStatusId);
    }
}
