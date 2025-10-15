package com.sprint.mission.repository;

import com.sprint.mission.entity.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    void update(User user);

    User findById(String id);
    void deleteById(String id);
    boolean existsById(String id);
    List<User> findByIds(String... ids);
    List<User> findAll();


}
