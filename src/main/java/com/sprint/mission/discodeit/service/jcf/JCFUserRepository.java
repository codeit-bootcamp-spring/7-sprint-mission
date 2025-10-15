package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
// 메모리(Mo) 기반 저장소 : 영속화 없음
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new LinkedHashMap<>();

    @Override
    public User save(User entity) {
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return data.remove(uuid) != null;
    }
}
