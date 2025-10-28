package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusStore = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id))
                .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));
    }

    @Override
    public void updateLoginTime(UUID id) {
        UserStatus status = findById(id);
        status.setUpdatedAt();

        userStatusStore.put(id, status);
    }

    @Override
    public void deleteById(UUID id) {
        userStatusStore.remove(id);
    }
}
