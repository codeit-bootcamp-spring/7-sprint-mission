package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserStatusRepository extends BaseFileRepository<UserStatus>
implements UserStatusRepository {
    public FileUserStatusRepository() {
        super(UserStatus.class);
    }

    //저장
    @Override
    public UserStatus save(UserStatus userStatus) {
        saveToFile(userStatus.getId(), userStatus);
        return userStatus;
    }

    //단일 조회
    @Override
    public Optional<UserStatus> findById(UUID id) {
        return loadFromFile(id);
    }

    //userId 로 조회
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAllFiles().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst();
    }

    //전체 목록
    @Override
    public List<UserStatus> findAll() {
        return findAllFiles();
    }

    //online 시간 update
    @Override
    public void updateOnlineAt(UUID id) {
        loadFromFile(id).ifPresent(userStatus -> {
            userStatus.updateOnlineAt();
            saveToFile(id, userStatus);
        });
    }

    //offline 시간 update
    @Override
    public void updateOfflineAt(UUID id) {
        loadFromFile(id).ifPresent(userStatus -> {
            userStatus.updateOfflineAt();
            saveToFile(id, userStatus);
        });
    }

    @Override
    public void update(UUID id) {
        loadFromFile(id).ifPresent(userStatus -> {
            userStatus.updateOnline();
            saveToFile(id, userStatus);
        });
    }

    @Override
    public void updateByUserId(UUID userId) {
        findAllFiles().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst().ifPresent(userStatus -> {
            userStatus.updateOnline();
            saveToFile(userStatus.getId(), userStatus);
        });
    }

    //삭제
    @Override
    public void delete(UUID userId) {
        deleteFile(userId);
    }

    @Override
    public boolean existsById(UUID id) {
        return fileExistsById(id);
    }
}
