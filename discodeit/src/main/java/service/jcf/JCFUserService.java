package service.jcf;

import entity.User;
import service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    // 싱글톤
    private static JCFUserService instance;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(User user) {
        if (data.containsKey(user.getId())) {
            data.put(user.getId(), user); // 기존 객체 덮어쓰기
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}