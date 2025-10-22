package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(String filePath) {
        this.userRepository = new FileUserRepository(filePath);
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