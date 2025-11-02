package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.file.FileIo;
import com.sprint.mission.discodeit.service.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileUserStatusRepository implements UserStatusRepository {
    private final String filename = "usersStatus";

    @Override
    public UserStatus save(UUID UserUUID) {
        UserStatus userStatus = new UserStatus(UserUUID);
        FileIo.save(filename,userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> find(UUID binaryId) {
        return FileIo.read(filename, binaryId, UserStatus.class);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
      return FileIo.read(filename, userId, UserStatus.class);
    }

    @Override
    public List<UserStatus> findAll() {
        return List.of();
    }

    @Override
    public void deleteByUserId(UUID binaryId) {
        String path = Path.RooT_PATH.getPath() + "/" + filename + "/" + binaryId + ".sav";
        File file = new File(path);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                throw new RuntimeException("파일 삭제 실패: " + file.getAbsolutePath());
            }
        }

    }

}
