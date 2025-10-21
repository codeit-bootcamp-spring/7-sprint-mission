package service;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(User user);
    User findById(UUID id);
    List<User> findAll();
    void update(User user);
    void delete(UUID id);
}
