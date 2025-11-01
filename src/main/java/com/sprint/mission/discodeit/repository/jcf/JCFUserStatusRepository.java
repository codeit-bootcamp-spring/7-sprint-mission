package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    // 스테이터스 데이터
    private final Map<UUID, UserStatus> data = new ConcurrentHashMap<>();
    
    // 저장
    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }
    
    //전체 목록
    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    //단일 조회
    @Override
    public UserStatus findById(UUID userId) {
        return data.get(userId);
    }

    //유저 id 로 조회
    @Override
    public UserStatus findByUserId(UUID userId) {
        return data.values().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst().orElse(null);
    }

    @Override
    public void updateOnlineAt(UUID id) {
        findById(id).updateOnlineAt();
    }

    @Override
    public void updateOfflineAt(UUID id) {
        findById(id).updateOfflineAt();
    }

    @Override
    public void update(UUID id) {
        UserStatus userStatus = findById(id);
        userStatus.updateOnline();
    }

    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        userStatus.updateOnline();
    }

    @Override
    public UserStatus delete(UUID userId) {
        UserStatus userStatus = findById(userId);
        data.remove(userId);
        return userStatus;
    }
}
