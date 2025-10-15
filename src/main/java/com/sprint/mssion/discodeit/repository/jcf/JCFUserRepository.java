package com.sprint.mssion.discodeit.repository.jcf;

import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> usersRepository = new HashMap<>();

    @Override
    public void save(User user) {
        usersRepository.put(user.getCommon().getId(), user);
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


}
