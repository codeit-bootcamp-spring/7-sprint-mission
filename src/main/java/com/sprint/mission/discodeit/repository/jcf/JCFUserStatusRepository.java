package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID,UserStatus> userStatusRepo;

    @Override
    public void resetRepository() {
        userStatusRepo.clear();
    }

    public JCFUserStatusRepository() {
        this.userStatusRepo = new HashMap<>();
    }

    @Override
    public UserStatus createUserStatus(UserStatus userStatus) {
        userStatusRepo.put(userStatus.getId(),userStatus);
        return userStatus;
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusRepo.remove(userStatusId);

    }

    @Override
    public void updateUserStatus(UserStatus userStatus) {
        userStatusRepo.put(userStatus.getId(),userStatus);

    }

    @Override
    public Optional<UserStatus> readUserStatus(UUID userStatusId) {
        return Optional.ofNullable(userStatusRepo.get(userStatusId));
    }

    @Override
    public List<UserStatus> readAllUserStatus() {
        return userStatusRepo.values().stream().toList();
    }

    @Override
    public boolean isUserStatusExist(UUID userStatusId) {
        return userStatusRepo.containsKey(userStatusId);
    }


}
