package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

//@Repository("JCFstatus")
public class JCFUserStatusRepository implements UserStatusRepository {


    //여기 uuid는 유저 uuid다
    private final Map<UUID,UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
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
        return List.of();
    }

    @Override
    public void deleteByUserId(UUID userId) {
       data.remove(userId);
    }

    @Override
    public List<UserStatus> findAllByUpdatedAtAfter(Instant since) {
        return List.of();
    }
}
