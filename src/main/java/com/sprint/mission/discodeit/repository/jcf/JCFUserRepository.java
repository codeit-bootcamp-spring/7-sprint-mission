package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JCFUserRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 List<User>를 저장소로 활용합니다.
 */
public class JCFUserRepository implements UserRepository {
    private final List<User> userStore = new ArrayList<>();

    private JCFUserRepository() {}

    private static JCFUserRepository instance = new JCFUserRepository();

    public static JCFUserRepository getInstance() {
        return instance;
    }

    @Override
    public void save(User user) {
        userStore.add(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userStore.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userStore.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phoneNum) {
        return userStore.stream().filter(u -> u.getPhoneNum().equals(phoneNum)).findFirst();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userStore.stream().filter(u -> u.getUserId().equals(userId)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore);
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < userStore.size(); i++) {
            if (userStore.get(i).getId().equals(user.getId())) {
                userStore.set(i, user);
                break;
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        userStore.removeIf(u -> u.getId().equals(id));
    }
}
