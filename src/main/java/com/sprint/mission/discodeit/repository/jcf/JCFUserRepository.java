package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    //싱글톤 구현
    private final static JCFUserRepository userRepository = new JCFUserRepository();

    private JCFUserRepository(){}

    public static JCFUserRepository getInstance(){
        return userRepository;
    }
    
    // 유저 데이터
    private final Map<UUID, User> data = new HashMap<>();

    // 저장
    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    //유저 목록
    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    //유저 Id
    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    //유저 email
    @Override
    public User findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    //유저 nickname
    @Override
    public List<User> findByNickname(String nickname) {
        return findAll().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .toList();
    }

    @Override
    public User update(UUID userId, String nickname, String password){
        return findById(userId).update(nickname, password);
    }

    @Override
    public void updateProfileImg(UUID userId, UUID profileImgId) {
        User user = findById(userId);
        user.updateProfile(profileImgId);
        save(user);
    }

    @Override
    public User delete(UUID userId) {
        User user = findById(userId);
        data.remove(userId);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return false;
    }
}
