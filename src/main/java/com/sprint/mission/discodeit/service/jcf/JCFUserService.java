package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    private static final JCFUserService jcfUserService = new JCFUserService();
    public static JCFUserService getInstance() {
        return jcfUserService;
    }

    @Override
    public User create(User user) {
        User u = VerifiedUtils.verifyNull(user);
        UUID id = VerifiedUtils.verifyNull(u.getId());
        if(data.containsKey(id)) {
            throw new IllegalStateException("User already exists: " + id);
        }
        data.put(id, u);
        return u;
    }

    @Override
    public User get(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        User user = data.get(id);
        if(user == null) {
            throw new NoSuchElementException("User not found: " + id);
        }
        return user;
    }

    @Override
    public List<User> getAll() { return new ArrayList<>(data.values()); }

    @Override
    public User update(User user) {
        User u = VerifiedUtils.verifyNull(user);
        UUID id = VerifiedUtils.verifyNull(u.getId());
        if(!data.containsKey(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        data.put(id, u);
        return u;
    }

    @Override
    public boolean delete(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return data.remove(id) != null;
    }

    // Online/Offline 전환
    @Override
    public User setUserState(UUID uuid, UserState userState) {
        User user = get(uuid);
        if(user.getUserState() != userState) {
            user.setUserState(userState);
        }
        return user;
    }
    // 이름으로 조회
    @Override
    public List<User> getUsersByName(String username) {
        String user = VerifiedUtils.verifyName(username);
        return data.values()
                .stream()
                .filter(u -> u.getUsername().equals(user))
                .toList();
    }

    // 이메일로 조회
    @Override
    public List<User> getUsersByEmail(String email) {
        String e = VerifiedUtils.verifyEmail(email);
        return data.values()
                .stream()
                .filter(u -> u.getEmail().equals(e))
                .toList();
    }

    // 특정 상태만 조회
    @Override
    public List<User> getUsersByState(UserState userState) {
        UserState state = VerifiedUtils.verifyNull(userState);
        return data.values()
                .stream()
                .filter( u -> u.getUserState() == state)
                .toList();
    }

    // 로그인
    @Override
    public User login(String email, String password) {
        String e = VerifiedUtils.verifyEmail(email);
        String p = VerifiedUtils.verifyPassword(password);
        User user = getUsersByEmail(e)
                .stream()
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException("User not found: " + email));
        if(!user.passwordMatch(p)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if(user.getUserState() != UserState.ONLINE) {
            user.setUserState(UserState.ONLINE);
        }
        return user;
    }
    // 로그아웃
    @Override
    public User logout(String email) {
        String e = VerifiedUtils.verifyEmail(email);
        User user = getUsersByEmail(e)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found: " + email));
        if(user.getUserState() == UserState.OFFLINE) {
            return user;
        }
        user.setUserState(UserState.OFFLINE);
        return user;
    }
}
