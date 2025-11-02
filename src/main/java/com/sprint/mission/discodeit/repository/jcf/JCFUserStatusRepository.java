package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Optional<UserStatus> findById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    //유저 id 로 조회
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void updateOnlineAt(UUID id) {

        data.get(id).updateOnlineAt();
    }

    @Override
    public void updateOfflineAt(UUID id) {
        data.get(id).updateOfflineAt();
    }

    @Override
    public void update(UUID id) {
        data.get(id).updateOnline();
    }

    @Override
    public void updateByUserId(UUID userId) {
        data.values().stream().filter(userStatus ->userStatus.getUserId().equals(userId))
        .findFirst().ifPresent(userStatus -> {
            userStatus.updateOnline();
            save(userStatus);
        });
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }
}
