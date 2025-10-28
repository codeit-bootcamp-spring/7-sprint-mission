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

    @Override
    public User update(UUID userId, String nickname, String password){
        return findById(userId).update(nickname, password);
    }

    @Override
    public User delete(UUID userId) {
        User user = findById(userId);
        data.remove(userId);
        return user;
    }
}
