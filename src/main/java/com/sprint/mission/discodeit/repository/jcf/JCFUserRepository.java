package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.domain.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> store = new HashMap<>();

    @Override
    public void save(User user){
        UUID key = user.getId();
        store.put(key, user);

    }
    @Override
    public void remove(User user){
        UUID userId = user.getId();
        store.remove(userId);
    }
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email){
        return store.values().stream().filter(user -> user.getEmail().equals(email)).findAny();

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return store.values().stream().filter(user -> user.getUsername().equals(username)).findAny();
    }


    @Override
    public List<User> findAll() {
        return List.copyOf(store.values().stream().toList());
    }
}
