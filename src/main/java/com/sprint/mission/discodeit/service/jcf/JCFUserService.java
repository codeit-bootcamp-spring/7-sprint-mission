package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new JCFUserRepository();
    }

    @Override
    public User create(String name, String email) {
        User u = new User(name, email);
        userRepository.save(u);
        return u;
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID id, String name, String email) {
        User u = userRepository.findById(id);
        if (u != null) {
            u.update(name, email);
            userRepository.save(u);
        }
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}