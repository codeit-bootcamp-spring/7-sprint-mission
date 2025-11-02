package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    // 사용자 생성 메서드
    // 1. 입력값 유효성 검사 (username, email, password 비어있으면 예외 발생)
    // 2. 저장 과정에서 오류가 발생할 경우 예외를 캐치하여 로그를 남기고 다시 던집니다.
    @Override
    public User create(String username, String email, String password) {
        // 입력값 검증 (공백, null 여부 확인)
        if (username == null || username.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            throw new IllegalArgumentException("username, email, password는 비어있을 수 없습니다.");
        }
        try {
            User user = new User(username, email, password);
            User saved = userRepository.save(user);
            // 정상처리 결과 반환
            return saved;
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 예외를 다시 던져 상위 계층에 알림.
            System.err.println("[ERROR] 사용자 생성 실패: " + e.getMessage());
            throw e;
        }
    }

    // 사용자 단건 조회 메서드
    // 1. userId가 null인 경우 IllegalArgumentException 발생
    // 2. 존재하지 않는 경우 NoSuchElementException 발생
    // 3. 예외 발생 시 로그를 남기고 재전달합니다.
    @Override
    public User find(UUID userId) {
        // 입력값 검증 (공백, null 여부 확인)
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
            // ✅ 정상적으로 처리되어 결과를 반환합니다.
            return user;
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 예외를 다시 던져 상위 계층에 알림.
            System.err.println("[ERROR] 사용자 단건 조회 실패: " + e.getMessage());
            throw e;
        }
    }

    // 전체 사용자 목록 조회 메서드
    // 1. 저장소 접근 시 발생할 수 있는 예외를 캐치하여 로그로 남깁니다.
    @Override
    public List<User> findAll() {
        try {
            List<User> users = userRepository.findAll();
            // 정상적으로 처리되어 결과를 반환.
            return users;
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 예외를 다시 던져 상위 계층에 알림.
            System.err.println("[ERROR] 전체 사용자 목록 조회 실패: " + e.getMessage());
            throw e;
        }
    }

    // 사용자 정보 수정 메서드
    // 1. userId 및 새 입력값 검증
    // 2. 존재하지 않으면 NoSuchElementException 발생
    // 3. 업데이트 중 오류 발생 시 로그를 남기고 재전달합니다.
    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        // 입력값 검증 (공백, null 여부 확인)
        if (userId == null ||
                newUsername == null || newUsername.isBlank() ||
                newEmail == null || newEmail.isBlank() ||
                newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("userId, newUsername, newEmail, newPassword는 비어있을 수 없습니다.");
        }
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
            user.update(newUsername, newEmail, newPassword);
            User saved = userRepository.save(user);
            // 정상적으로 처리되어 결과를 반환.
            return saved;
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 예외를 다시 던져 상위 계층에 알림.
            System.err.println("[ERROR] 사용자 정보 수정 실패: " + e.getMessage());
            throw e;
        }
    }

    // 사용자 삭제 메서드
    // 1. userId 검증
    // 2. 존재하지 않으면 NoSuchElementException 발생
    // 3. 삭제 중 예외 발생 시 로그를 남기고 재전달합니다.
    @Override
    public void delete(UUID userId) {
        // 입력값 검증 (공백, null 여부 확인)
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        try {
            if (!userRepository.existsById(userId)) {
                throw new NoSuchElementException("User with id " + userId + " not found");
            }
            userRepository.deleteById(userId);
            // 정상적으로 처리되어 결과를 반환.
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 예외를 다시 던져 상위 계층에 알림.
            System.err.println("[ERROR] 사용자 삭제 실패: " + e.getMessage());
            throw e;
        }
    }
}
