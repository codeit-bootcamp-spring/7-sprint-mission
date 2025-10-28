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

    private JCFUserRepository() {}

    private static JCFUserRepository instance = new JCFUserRepository();

    public static JCFUserRepository getInstance() { return instance; }

    @Override
    public void save(User user) {
        userStore.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        if(!isExist(id)){
            throw new IllegalArgumentException("해당 UUID를 가진 유저가 존재하지 않습니다.");
        }

        // 해당 key(UUID) 값에 value(유저)가 null로 저장되어 있는 경우 예외 발생
        return Optional.ofNullable(userStore.get(id))
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID를 가진 유저가 존재하지 않습니다."));
    }

    @Override
    public User findByEmail(String email) {
        return userStore.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 사용하는 유저가 존재하지 않습니다."));
    }

    @Override
    public User findByPhone(String phoneNum) {
        return userStore.values().stream()
                .filter(u -> u.getPhoneNum().equals(phoneNum))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 전화번호를 사용하는 유저가 존재하지 않습니다."));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userStore.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public void update(User user) {
        if(isExist(user.getId())){
            userStore.replace(user.getId(), user);
        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않아 정보 수정에 실패하였습니다.");
        }
    }

    @Override
    public void deleteById(UUID id) {
        if(!isExist(id)) {
            throw new IllegalArgumentException("삭제할 유저가 존재하지 않습니다.");
        }
        userStore.remove(id);
    }

    @Override
    public boolean isExist(UUID id) {
        return userStore.containsKey(id);
    }

    @Override
    public void existsByNickName(String NickName) {
        if(findAll().stream().anyMatch(u -> u.getNickName().equals(NickName))){
            throw new IllegalArgumentException("이미 사용 중인 nickname입니다.");
        }
    }

    @Override
    public void existsByEmail(String email) {
        if(findAll().stream().anyMatch(u -> u.getEmail().equals(email))){
            throw new IllegalArgumentException("이미 사용 중인 email입니다.");
        }
    }
}
