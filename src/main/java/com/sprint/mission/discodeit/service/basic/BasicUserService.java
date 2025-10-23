package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        User u = VerifiedUtils.verifyNull(user);
        UUID id = VerifiedUtils.verifyNull(u.getId());
        if(userRepository.findById(id).isPresent()) {
            throw new IllegalStateException("User already exists: " + id);
        }
        return userRepository.save(u);
    }

    @Override
    public User get(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        User u = VerifiedUtils.verifyNull(user);
        UUID id = VerifiedUtils.verifyNull(u.getId());
        userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        return userRepository.save(u);
    }

    @Override
    public boolean delete(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return userRepository.deleteById(id);
    }

    // Online/Offline 전환
    @Override
    public User setUserState(UUID uuid, UserState userState) {
        User user = get(uuid);
        if(user.getUserState() != userState) {
            user.setUserState(userState);
            userRepository.save(user);
        }
        return user;
    }
    // 이름으로 조회
    @Override
    public List<User> getUsersByName(String username) {
        String user = VerifiedUtils.verifyName(username);
        return userRepository.findByName(user);
    }

    // 이메일로 조회
    @Override
    public Optional<User> getUsersByEmail(String email) {
        String e = VerifiedUtils.verifyEmail(email);
        return userRepository.findByEmail(e);
    }

    // 특정 상태만 조회
    @Override
    public List<User> getUsersByState(UserState userState) {
        UserState state = VerifiedUtils.verifyNull(userState);
        return userRepository.findByState(state);
    }

    // 로그인
    @Override
    public User login(String email, String password) {
        String e = VerifiedUtils.verifyEmail(email);
        String p = VerifiedUtils.verifyPassword(password);
        User user = getUsersByEmail(e)
                .orElseThrow( () -> new NoSuchElementException("User not found: " + email));
        if(!user.passwordMatch(p)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if(user.getUserState() != UserState.ONLINE) {
            user.setUserState(UserState.ONLINE);
            userRepository.save(user);
        }
        return user;
    }
    // 로그아웃
    @Override
    public User logout(String email) {
        String e = VerifiedUtils.verifyEmail(email);
        User user = getUsersByEmail(e)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + email));
        if(user.getUserState() == UserState.OFFLINE) {
            return user;
        }
        user.setUserState(UserState.OFFLINE);
        userRepository.save(user);
        return user;
    }
}
