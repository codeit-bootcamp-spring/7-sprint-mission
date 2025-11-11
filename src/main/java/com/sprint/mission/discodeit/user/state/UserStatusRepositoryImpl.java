package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import com.sprint.mission.discodeit.config.enums.DataKey;
import com.sprint.mission.discodeit.config.enums.Status;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserStatusRepositoryImpl extends BaseRepositoryImpl<UserStatus, UUID> implements UserStatusRepository {
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
         Optional<UserStatus> check = dataMap.values().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst();
        return check;
    }

    @Override
    public Optional<UserStatus> findByUserIdNonDel(UUID userId) {
        return dataMap.values().stream()
                .filter(us -> !us.isDeleted() && us.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return dataMap.values().stream()
                .anyMatch(us -> us.getUserId()
                        .equals(userId));
    }

    @Override
    public boolean existsByUserIdNonDel(UUID userId) {
        return dataMap.values().stream()
                .anyMatch(us -> !us.isDeleted() && us.getUserId()
                        .equals(userId));
    }

    @Override
    public List<UserStatus> findAllByState(Status currentStatus) {
        return dataMap.values().stream()
                .filter(us -> us.getCurrentStatus().equals(currentStatus))
                .toList();
    }

    @Override
    public DataKey getDataKey() {
        return DataKey.USER_STATUS;
    }
}
