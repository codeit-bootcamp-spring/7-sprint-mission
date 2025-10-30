package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    // 스테이터스 데이터
    private final Map<UUID, UserStatus> data = new HashMap<>();
    
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
    public UserStatus updateOnlineAt(UUID id) {
        return findById(id).updateOnlineAt();
    }

    @Override
    public UserStatus updateOfflineAt(UUID id) {
        return findById(id).updateOfflineAt();
    }

    @Override
    public UserStatus delete(UUID userId) {
        UserStatus userStatus = findById(userId);
        data.remove(userId);
        return userStatus;
    }
}
