package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserStatusRepository extends BaseJCFRepository<UserStatus> implements UserStatusRepository {
    // 저장
    @Override
    public UserStatus save(UserStatus userStatus) {
        beforeModify();
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
        beforeModify();
        data.get(id).updateOnlineAt();
        save(data.get(id));
    }

    @Override
    public void updateOfflineAt(UUID id) {
        beforeModify();
        data.get(id).updateOfflineAt();
        save(data.get(id));
    }

    @Override
    public void update(UUID id) {
        beforeModify();
        data.get(id).updateOnline();
        save(data.get(id));
    }

    @Override
    public void updateByUserId(UUID userId) {
        beforeModify();
        data.values().stream().filter(userStatus ->userStatus.getUserId().equals(userId))
        .findFirst().ifPresent(userStatus -> {
            userStatus.updateOnline();
            save(userStatus);
        });
    }

    @Override
    public void delete(UUID userId) {
        beforeModify();
        data.remove(userId);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
