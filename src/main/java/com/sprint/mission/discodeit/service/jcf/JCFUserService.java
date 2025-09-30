package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * UserService 인터페이스의 메모리 기반 구현체입니다.
 * 비즈니스 로직을 수행하며, 데이터 영속성은 UserRepository에 위임합니다.
 */
public class JCFUserService implements UserService {

    private final UserRepository userRepository;

    /**
     * JCFUserService의 생성자입니다.
     * 의존성 주입(Dependency Injection)을 통해 UserRepository의 구현체를 받습니다.
     *
     * @param userRepository 사용자 데이터에 접근하기 위한 Repository
     */
    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 사용자 이름이 중복되는 경우, 비즈니스 규칙에 따라 IllegalStateException을 발생시킵니다.
     */
    @Override
    public User createUser(String username, String password, String email, String nickname, String phoneNum) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 사용자 이름입니다: " + username);
        }
        User newUser = User.create(username, password, email, nickname, phoneNum);
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * {@inheritDoc}
     * <p>
     * ID로 사용자를 조회하고, 찾은 엔티티의 {@link User#updateProfile(String, String, String)} 메서드를
     * 호출하여 상태를 변경한 뒤, 저장소에 다시 저장합니다.
     */
    @Override
    public User updateProfile(UUID userId, String nickname, String email, String phoneNum) {
        User user = findById(userId);
        user.updateProfile(nickname, email, phoneNum);
        userRepository.save(user);
        return user;
    }

    @Override
    public void goOnline(UUID userId) {
        User user = findById(userId);
        user.goOnline();
        userRepository.save(user);
    }

    @Override
    public void goOffline(UUID userId) {
        User user = findById(userId);
        user.goOffline();
        userRepository.save(user);
    }

    @Override
    public void setAway(UUID userId) {
        User user = findById(userId);
        user.setAway();
        userRepository.save(user);
    }

    @Override
    public void setDoNotDisturb(UUID userId) {
        User user = findById(userId);
        user.setDoNotDisturb();
        userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        User user = findById(userId);
        user.changePassword(newPassword);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Repository는 데이터의 존재 유무를 {@link java.util.Optional}로 보고하고,
     * Service는 "사용자가 없으면 안 된다"는 비즈니스 규칙에 따라 예외를 처리합니다.
     */
    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));
    }

    /**
     * {@inheritDoc}
     * <p>
     * 조회된 사용자가 없는 것은 정상적인 상황이므로 예외를 던지지 않고,
     * 클라이언트가 안전하게 사용할 수 있도록 빈 리스트를 반환합니다.
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 데이터를 삭제하기 전에 해당 데이터가 실제로 존재하는지 먼저 확인하여,
     * 존재하지 않을 경우 예외를 발생시키는 방어 로직을 포함합니다.
     */
    @Override
    public void deleteById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("삭제할 사용자가 존재하지 않습니다: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 사용자를 조회한 뒤, 상태 확인 책임은 {@link User} 엔티티의 메서드에 위임합니다.
     */
    @Override
    public boolean isOnline(UUID userId) {
        return findById(userId).isOnline();
    }

    @Override
    public boolean isOffline(UUID userId) {
        return findById(userId).isOffline();
    }

    @Override
    public boolean isAway(UUID userId) {
        return findById(userId).isAway();
    }

    @Override
    public boolean isDoNotDisturb(UUID userId) {
        return findById(userId).isDoNotDisturb();
    }
}