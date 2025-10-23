package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    //싱글톤 구현
    private final static JCFUserRepository userRepository = getInstance();

    private JCFUserRepository(){}

    public static JCFUserRepository getInstance(){
        return userRepository;
    }
    
    //유저 데이터
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {

        return user;
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(data);
    }

    @Override
    public User findById(UUID id) {
        return data.;
    }

    @Override
    public User updateNickname(String nickname) {
        return null;
    }

    @Override
    public User updatePassword(String password) {
        return null;
    }

    @Override
    public User delete(UUID userId) {
        return null;
    }
}
