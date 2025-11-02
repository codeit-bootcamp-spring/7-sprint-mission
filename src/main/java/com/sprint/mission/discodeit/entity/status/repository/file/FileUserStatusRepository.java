package com.sprint.mission.discodeit.entity.status.repository.file;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.entity.status.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileBaseRepository;
import org.springframework.stereotype.Repository;

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
