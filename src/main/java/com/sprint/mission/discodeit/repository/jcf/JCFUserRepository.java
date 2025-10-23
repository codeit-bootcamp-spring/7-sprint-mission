package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    private static JCFUserRepository instance;

    private JCFUserRepository() {}

    public static JCFUserRepository getInstance() {
        if (instance == null) {
            instance = new JCFUserRepository();
        }
        return instance;
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID uuid) {
        users.remove(uuid);
    }
}
