package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.common.Event;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.EventService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * UserService 인터페이스의 메모리(JCF) 기반 구현체입니다.
 */
public class JCFUserService extends JCFBaseService<User, UUID, UserRepository> implements UserService {

    // JCFBaseService의 repository 필드와 별도로 UserRepository 타입의 필드를 두어
    // findByUsername과 같은 고유 메서드에 쉽게 접근할 수 있도록 합니다.
    private final UserRepository userRepository;
    private final EventService eventService;

    public JCFUserService(UserRepository userRepository, JCFEventService eventService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    @Override
    public User createUser(String username, String password, String email, String nickname, String phoneNum) {
        // 1. '삭제된 기록을 포함하여' 같은 이름의 사용자가 있는지 Repository에서 직접 조회합니다.
        Optional<User> existingUser = userRepository.findByUsername(username); // (이 메서드는 새로 추가해야 합니다)

        if (existingUser.isPresent()) {
            // 2a. 기존 사용자가 있는 경우
            User user = existingUser.get();
            if (user.isDeleted()) {
                // 탈퇴한 사용자라면, 정보를 복원하고 업데이트합니다.
                user.restore();
                user.changePassword(password); // 비밀번호는 새로 설정
                user.updateProfile(nickname, email, phoneNum); // 프로필 정보 업데이트
                userRepository.save(user);
                return user;
            } else {
                // 이미 활성 상태인 사용자라면, 예외를 발생시킵니다.
                throw new IllegalStateException("이미 존재하는 사용자 이름입니다: " + username);
            }
        } else {
            // 2b. 기존 사용자가 전혀 없는 경우 (신규 가입)
            User newUser = User.create(username, password, email, nickname, phoneNum);
            userRepository.save(newUser);
            return newUser;
        }
    }

    @Override
    public User updateProfile(UUID userId, String nickname, String email, String phoneNum) {
        // 1. ID로 사용자를 찾아(Find),
        User user = findByIdNonDel(userId);
        // 2. 엔티티의 상태를 변경(Modify)하고 저장(Save)합니다.
        user.updateProfile(nickname, email, phoneNum);
        userRepository.save(user);
        return user;
    }

    @Override
    public void goOnline(UUID userId) {
        User user = findByIdNonDel(userId);
        user.goOnline();
        userRepository.save(user);
        if (eventService != null) {
            // User 엔티티 대신 필요한 정보만 담은 AuthUser를 이벤트 데이터로 사용
            AuthUser authUser = AuthUser.from(user);
            Event<AuthUser> event = new Event<>(Event.EventType.USER_STATUS_CHANGED, authUser);
            eventService.publishEvent(event);
        }
    }

    @Override
    public void goOffline(UUID userId) {
        User user = findByIdNonDel(userId);
        user.goOffline();
        userRepository.save(user);

        if (eventService != null) {
            AuthUser authUser = AuthUser.from(user);
            Event<AuthUser> event = new Event<>(Event.EventType.USER_STATUS_CHANGED, authUser);
            eventService.publishEvent(event);
        }
    }

    @Override
    public void setAway(UUID userId) {
        User user = findByIdNonDel(userId);
        user.setAway();
        userRepository.save(user);
    }

    @Override
    public void setDoNotDisturb(UUID userId) {
        User user = findByIdNonDel(userId);
        user.setDoNotDisturb();
        userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        User user = findByIdNonDel(userId);
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
        return findByIdNonDel(userId).isOnline();
    }

    @Override
    public boolean isOffline(UUID userId) {
        return findByIdNonDel(userId).isOffline();
    }



    @Override
    public boolean isAway(UUID userId) {
        return findByIdNonDel(userId).isAway();
    }

    @Override
    public boolean isDoNotDisturb(UUID userId) {
        return findByIdNonDel(userId).isDoNotDisturb();
    }

    @Override
    public User findByUsernameNonDel(String username) {
        return repository.findByUsernameNonDel(username).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username));
    }

    @Override
    public boolean existsByUsernameNonDel(String username) {
        return repository.existsByUsernameNonDel(username);
    }

}