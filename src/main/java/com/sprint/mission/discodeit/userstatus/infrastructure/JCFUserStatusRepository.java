package com.sprint.mission.discodeit.userstatus.infrastructure;

import com.sprint.mission.discodeit.server.domain.Server;
import com.sprint.mission.discodeit.userstatus.application.UserStatusRepository;
import com.sprint.mission.discodeit.userstatus.domain.UserStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {


    private final Map<UUID, UserStatus> store = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        UUID key = userStatus.getId();
        store.put(key, userStatus);
    }

    @Override
    public void remove(UserStatus userStatus) {
        UUID findChannelId = userStatus.getId();
        store.remove(findChannelId);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return store.values().stream().filter(us->us.getUserId().equals(userId)).findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        return List.copyOf(store.values().stream().toList());
    }


}
