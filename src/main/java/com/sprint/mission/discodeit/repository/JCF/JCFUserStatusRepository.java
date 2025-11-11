package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.InterfaceUserStatusRepository;

import java.util.*;

//@Repository
public class JCFUserStatusRepository implements InterfaceUserStatusRepository {


    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(UserStatus model) {
        this.data.put(model.getId(), model);
    }

    @Override
    public boolean deleteById(UUID id) {
        this.data.remove(id);
        return false;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<UserStatus>> findAll() {
        List<UserStatus> list = this.data.values().stream().toList();
        return Optional.ofNullable(list);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userID) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public boolean existsByName(String name) {
//        List<UserStatus> list = this.data.values().stream().toList();
//        list.stream().anyMatch(userStatus -> userStatus.)
//        this.data.values().stream().anyMatch(userStatus -> userStatus.)
        return false;
    }
}
