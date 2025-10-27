package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.exceptions.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static final JCFUserRepository instance = new JCFUserRepository();
    private static final Map<String, User> data = new HashMap<>();// 유저 id, User객체 (id검색을 빠르게 하기 위함)

    private JCFUserRepository(){}

    public static JCFUserRepository getInstance() {
        return instance;
    }

    @Override
    public void save(User user) {
        if(isExsistId(user.getUserId()))
            throw new UserAlreadyExistsException(user.getUserId());
        data.put(user.getUserId(), user);
    }

    @Override
    public void update(User user) {
        if(!isExsistId(user.getUserId()))
            throw new UserNotFoundException(user.getUserId());
        data.put(user.getUserId(), user);
    }


    @Override
    public User findById(String id) {
        if(isExsistId(id))
            throw new UserNotFoundException(id);
        return data.get(id);
    }

    @Override
    public void deleteById(String id) {
        if(!isExsistId(id))
            throw new UserNotFoundException(id);
        data.remove(id);
    }

    @Override
    public boolean isExsistId(String id) {
        return data.containsKey(id);
    }

    @Override
    public List<User> findByIds(String... ids) {
        return Arrays.stream(ids)
                .filter(this::isExsistId)
                .map(data::get)
                .sorted(Comparator.comparing(User::getUserId))
                .toList();
    }

    @Override
    public List<User> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(User::getUserId))
                .toList();
    }

}
