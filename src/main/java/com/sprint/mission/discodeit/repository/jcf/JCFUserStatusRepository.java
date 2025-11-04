package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFUserStatusRepository implements UserStatusRepository {
    @Override
    public Optional<UserStatus> find(UUID binaryId) {
        return Optional.empty();
    }

    //여기 uuid는 유저 uuid다
    private final Map<UUID,UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new ConcurrentHashMap<>();
    }


    @Override
    public UserStatus save(UUID userUUID) {
        UserStatus status = new UserStatus(userUUID);
        //   status.setUpdatedAt(Instant.now());
        this.data.put(userUUID,status);
        return status;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(this.data.get(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public void deleteByUserId(UUID userId) {
       data.remove(userId);
    }

}
