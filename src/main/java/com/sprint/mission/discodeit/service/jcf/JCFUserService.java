package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * UserService 인터페이스의 메모리(JCF) 기반 구현체입니다.
 */
public class JCFUserService extends JCFBaseService<User, UUID, UserRepository> implements UserService {

    // JCFBaseService의 repository 필드와 별도로 UserRepository 타입의 필드를 두어
    // findByUsername과 같은 고유 메서드에 쉽게 접근할 수 있도록 합니다.
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String password, String email, String nickname, String phoneNum) {
        // 서비스 계층의 핵심 책임: 비즈니스 규칙(예: 사용자 이름 중복 불가)을 검증합니다.
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 사용자 이름입니다: " + username);
        }
        // 실제 객체 생성 책임은 User 엔티티의 정적 팩토리 메서드에 위임합니다.
        User newUser = User.create(username, password, email, nickname, phoneNum);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User updateProfile(UUID userId, String nickname, String email, String phoneNum) {
        // 1. ID로 사용자를 찾아(Find),
        User user = findById(userId);
        // 2. 엔티티의 상태를 변경(Modify)하고 저장(Save)합니다.
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

    @Override
    public User findByUsername(String username) {
        // Repository의 조회 결과를 처리하여, 없으면 비즈니스 규칙에 따라 예외를 던집니다.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username));
    }



    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isOnline(UUID userId) {
        // 상태 확인의 책임은 서비스가 아닌, 엔티티 자신에게 위임합니다.
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