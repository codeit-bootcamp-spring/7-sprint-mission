package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
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
    public UserStatus findById(UUID id) {
        return loadFromFile(id);
    }

    //userId 로 조회
    @Override
    public UserStatus findByUserId(UUID userId) {
        return findAllFiles().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst().orElse(null);
    }

    //전체 목록
    @Override
    public List<UserStatus> findAll() {
        return findAllFiles();
    }

    //online 시간 update
    @Override
    public UserStatus updateOnlineAt(UUID id) {
        UserStatus userStatus = loadFromFile(id);
        if(userStatus == null){
            throw new RuntimeException("UserStatus with id=" + id + " not found");
        }
        userStatus.updateOnlineAt();
        saveToFile(id, userStatus);
        return userStatus;
    }

    //offline 시간 update
    @Override
    public UserStatus updateOfflineAt(UUID id) {
        UserStatus userStatus = loadFromFile(id);
        if(userStatus == null){
            throw new RuntimeException("UserStatus with id=" + id + " not found");
        }
        userStatus.updateOfflineAt();
        saveToFile(id, userStatus);
        return userStatus;
    }

    //삭제
    @Override
    public UserStatus delete(UUID userId) {
        UserStatus userStatus = loadFromFile(userId);
        deleteFile(userId);
        return userStatus;
    }
}
