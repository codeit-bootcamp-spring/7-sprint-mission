package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> usersRepository = new HashMap<>();

    @Override
    public void save(User user) {
        usersRepository.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(usersRepository.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersRepository.values());
    }

    @Override
    public void deleteById(UUID id) {
        usersRepository.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return usersRepository.containsKey(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return usersRepository.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
