package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.Optional;
import java.util.UUID;

public class FileUserStatusRepository extends FileBaseRepository<UserStatus> implements UserStatusRepository {

    private static final String USER_STATUS_DATA_FILE = "userStatusData.ser";

    public FileUserStatusRepository(String basePath) {
        super(basePath + "/" + USER_STATUS_DATA_FILE);
    }

    @Override
    public Optional<UserStatus> findStatusByUserId(UUID userId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteStatusByUserId(UUID userId) {
        data.values().removeIf(status -> status.getUserId().equals(userId) );
    }
}
