package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository {
    private final FileUtil fileUtil;
    public FileUserStatusRepository(@Qualifier("userStatusFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void save(UserStatus userStatus) {
        fileUtil.saveRepository(userStatus);
    }

    public Optional<UserStatus> findById(UUID id) {
        Optional<UserStatus> optionalStatus = fileUtil.findAll().stream().map(userStatus -> (UserStatus) userStatus).filter(userStatus -> userStatus.getId().equals(id)).findFirst();
        return optionalStatus;
    }

    public Optional<UserStatus> findByUserId(UUID userID) {
        Optional<UserStatus> optionalStatus = fileUtil.findAll().stream().map(userStatus -> (UserStatus) userStatus).filter(userStatus -> userStatus.getUserId().equals(userID)).findFirst();
        return optionalStatus;
    }

    public Optional<List<UserStatus>> findAll() {
        return Optional.ofNullable(fileUtil.findAll().stream()
                .map(userStatus -> (UserStatus)userStatus)
                .toList());
    }

    public void deleteById(UUID id) {
        fileUtil.deleteRepository(id);
    }
}
