package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;

import static com.sprint.mission.discodeit.global.utils.FileIOHandler.*;

public class FileUserStatusRepository implements UserStatusRepository {
    //파일 저장 필요
    private final Map<UUID, UserStatus> userStatusStore = new HashMap<>();
    private final String filePath;

    public FileUserStatusRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath, userStatusStore);
    }

    @Override
    public void save(UserStatus userStatus) {
        // 유저는 하나의 UserStatus를 가지기 때문에 key를 유저 UUID로 설정
        userStatusStore.put(userStatus.getUserId(), userStatus);
        saveToFile(filePath, userStatusStore);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusStore.values());
    }

    @Override
    public void update(UserStatus status) {
        userStatusStore.replace(status.getUserId(), status);
        saveToFile(filePath, userStatusStore);
    }

    @Override
    public void deleteById(UUID id) {
        userStatusStore.remove(id);
        saveToFile(filePath, userStatusStore);
    }
}
