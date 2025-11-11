package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

/**
 * JCFUserRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 List<User>를 저장소로 활용합니다.
 */
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userStore = new HashMap<>();

    @Override
    public void save(User user) {
        userStore.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStore.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phoneNum) {
        return userStore.values().stream()
                .filter(u -> u.getPhoneNum().equals(phoneNum))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userStore.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public void update(User user) {
        userStore.replace(user.getId(), user);
    }

    @Override
    public void deleteById(UUID id) {
        userStore.remove(id);
    }
}
