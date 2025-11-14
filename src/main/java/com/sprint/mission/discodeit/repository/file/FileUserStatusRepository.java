package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.InterfaceUserStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository  implements InterfaceUserStatusRepository {
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
        Optional<UserStatus> optionalStatus = fileUtil.findAll().stream().map(userStatus -> (UserStatus) userStatus).filter(userStatus -> userStatus.getId().equals(id)).findFirst();
        return optionalStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userID) {
        Optional<UserStatus> optionalStatus = fileUtil.findAll().stream().map(userStatus -> (UserStatus) userStatus).filter(userStatus -> userStatus.getUserId().equals(userID)).findFirst();
        return optionalStatus;
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

    @Override
    public boolean existsById(UUID id) {
        return false; // Base 에 넣어놓는게 편해서.. 그냥 구현 ㅠ
    }

    @Override
    public boolean existsByName(String name) {
        return false; // Base 에 넣어놓는게 편해서.. 그냥 구현 ㅠ
    }
}
