package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    void update(User user);

    User findById(String id);
    void deleteById(String id);
    boolean isExsistId(String id);
    List<User> findByIds(String... ids);
    List<User> findAll();


}
