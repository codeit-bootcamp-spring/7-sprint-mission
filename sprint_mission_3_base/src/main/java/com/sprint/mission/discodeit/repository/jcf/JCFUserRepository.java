package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.UUID;

@Repository
@Primary
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new ConcurrentHashMap<>();

    // 기본 CRUD
    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    // 추가 메서드들 (UserRepository에 선언된 것과 정확히 일치해야 함)
    @Override
    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.values().stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst();
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return data.values().stream().anyMatch(u ->
                Objects.equals(u.getUsername(), username) || Objects.equals(u.getEmail(), email)
        );
    }
    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return findAll().stream()
                .filter(u -> Objects.equals(u.getUsername(), username)
                        && Objects.equals(u.getPassword(), password))
                .findFirst();
    }




}
