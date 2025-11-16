package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BaseInterfaceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository  implements BaseInterfaceRepository<UserStatus> {
    private final FileUtil fileUtil;
    public FileUserStatusRepository(@Qualifier("userStatusFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(UserStatus userStatus) {
        fileUtil.saveRepository(userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return fileUtil.findAll().stream()
            .map(userStatus -> (UserStatus) userStatus)
            .filter(userStatus -> userStatus.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userID) {
        return fileUtil.findAll().stream()
            .map(userStatus -> (UserStatus) userStatus)
            .filter(userStatus -> userStatus.getUserId().equals(userID)).findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return fileUtil.findAll().stream()
                .map(userStatus -> (UserStatus)userStatus)
                .toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        return fileUtil.deleteRepository(id);
    }
}
