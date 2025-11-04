package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class FileUserStatusRepository extends AbstractFileRepository<UserStatus, UUID> implements UserStatusRepository {

    private static final String FILE_PATH = "data" + File.separator + "userStatus.ser";

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }

    @Override
    protected UUID getId(UserStatus userStatus) {
        return userStatus.getId();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Optional<UserStatus> byUserId = findByUserId(userId);
        byUserId.ifPresent(userStatus -> delete(userStatus.getId()));
    }
}
