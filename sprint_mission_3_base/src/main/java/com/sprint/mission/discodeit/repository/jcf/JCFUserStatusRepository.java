package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.UUID;

@Repository
@Primary  // 같은 타입 구현이 여러 개일 때 이 구현을 우선 사용
public class JCFUserStatusRepository implements UserStatusRepository {


    private final Map<UUID, UserStatus> store = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> byUserId = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus us) {
        store.put(us.getId(), us);
        byUserId.put(us.getUserId(), us.getId());
        return us;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        UUID id = byUserId.get(userId);
        return Optional.ofNullable(id).map(store::get);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return byUserId.containsKey(userId);
    }

    @Override
    public void deleteById(UUID id) {
        var removed = store.remove(id);
        if (removed != null) {
            byUserId.remove(removed.getUserId());
        }
    }
}
