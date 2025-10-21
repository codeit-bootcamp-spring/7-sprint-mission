package com.sprint.mission.discodeit.repository.jcf;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


public class JCFUserRepository implements UserRepository {

    // 메모리에만 저장
    private final List<User> users = new ArrayList<>();


    private static final JCFUserRepository INSTANCE = new JCFUserRepository();
    public static JCFUserRepository getInstance() { return INSTANCE; }
    private JCFUserRepository() {}

    @Override
    public User create(String userId, String password, String userName, String userNickname) {
        User user = new User(userId, password, userName, userNickname);
        users.add(user);
        return user;
    }

    @Override
    public User read(UUID userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(users);
    }

    @Override
    public boolean delete(UUID userId) {
        return users.removeIf(u -> u.getId().equals(userId));
    }

    @Override
    public User updateName(UUID uuid, String userName) {
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        u.setUserName(userName);
        return u;
    }

    @Override
    public User updateNickName(UUID uuid, String userNickname) {
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        u.setUserNickname(userNickname);
        return u;
    }

    @Override
    public User update(UUID uuid, Consumer<User> updater) {
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        updater.accept(u);
        return u;
    }
}