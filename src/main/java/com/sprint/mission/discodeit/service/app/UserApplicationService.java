package com.sprint.mission.discodeit.service.app;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.user.ProfileImageCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService; //

    // ===== 공통 검증/유틸 메서드 (중복 제거) =====
    private void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "은 비어 있을 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("email 형식이 올바르지 않습니다.");
        }
    }

    private void validateProfileImage(ProfileImageCreateRequest img) {
        if (img == null) return; // 선택값
        if (img.data() == null || img.data().length == 0) {
            throw new IllegalArgumentException("프로필 이미지 데이터가 비어 있습니다.");
        }
        if (img.filename() == null || img.filename().isBlank()) {
            throw new IllegalArgumentException("프로필 이미지 파일명이 비어 있습니다.");
        }
        if (img.contentType() == null || img.contentType().isBlank()) {
            throw new IllegalArgumentException("프로필 이미지 contentType이 비어 있습니다.");
        }
    }

    /**
     * username/email 중복 방지 검사.
     * excludeUserId가 null이면 전체에서 중복 검사, 아니면 해당 ID는 제외하고 검사.
     */
    private void ensureUniqueUsernameEmail(String name, String email, java.util.UUID excludeUserId) {
        var users = userService.findAll();
        boolean usernameDup = users.stream()
                .anyMatch(u -> (excludeUserId == null || !u.getId().equals(excludeUserId)) && u.getUsername().equals(name));
        if (usernameDup) {
            throw new IllegalArgumentException("이미 사용 중인 username 입니다: " + name);
        }
        boolean emailDup = users.stream()
                .anyMatch(u -> (excludeUserId == null || !u.getId().equals(excludeUserId)) && u.getEmail().equalsIgnoreCase(email));
        if (emailDup) {
            throw new IllegalArgumentException("이미 사용 중인 email 입니다: " + email);
        }
    }

    // 사용자 생성 (DTO in > DTO out)
    public UserResponse createUser(UserCreateRequest req) {
        // 입력값 검증 (DTO 활용)
        requireNonNull(req, "요청 본문이 null일 수 없습니다.");
        validateNotBlank(req.name(), "name");
        validateEmail(req.email());
        validateNotBlank(req.password(), "password");
        validateProfileImage(req.profileImage());

        // username/email 중복 방지
        ensureUniqueUsernameEmail(req.name(), req.email(), null);

        try {
            // 4. 유저 생성
            User created = userService.create(req.name(), req.email(), req.password());

            // 5. UserStatus 생성
            // (UserStatus): 구현체/리포지토리 준비 후 활성화할 것
            // try {
            //     var status = new com.sprint.mission.discodeit.entity.UserStatus(
            //             created.getId(),
            //             java.time.Instant.now(),
            //             java.time.Instant.now(),
            //             java.time.Instant.now()
            //     );
            //     userStatusRepository.save(status);
            // } catch (Exception e) {
            //     log.error("UserStatus 생성 실패: userId={}", created.getId(), e);
            // }

            // 6. 선택적 프로필 이미지 등록
            if (req.profileImage() != null) {
                // (BinaryContent): 구현체/리포지토리 준비 후 활성화할 것
                // var img = req.profileImage();
                // try {
                //     var bin = new com.sprint.mission.discodeit.entity.BinaryContent(
                //             created.getId(),
                //             null,
                //             img.filename(),
                //             img.contentType(),
                //             img.data(),
                //             java.time.Instant.now()
                //     );
                //     binaryContentRepository.save(bin);
                // } catch (Exception e) {
                //     log.error("프로필 이미지 저장 실패: userId={}", created.getId(), e);
                // }
            }

            return UserMapper.toResponse(created);
        } catch (Exception e) {
            log.error("사용자 생성 중 오류: name={}, email={}", req.name(), req.email(), e);
            throw e;
        }
    }

    // 단건 조회
    public UserResponse getUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        try {
            User found = userService.find(userId);
            return UserMapper.toResponse(found);
        } catch (NoSuchElementException e) {
            // 존재하지 않음은 별도 로깅 후 재전달
            log.warn("사용자 조회 실패(존재하지 않음): userId={}", userId);
            throw e;
        } catch (Exception e) {
            log.error("사용자 조회 중 오류: userId={}", userId, e);
            throw e;
        }
    }

    // 전체 조회
    public List<UserResponse> listUsers() {
        try {
            return userService.findAll().stream()
                    .map(UserMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("사용자 전체 조회 중 오류", e);
            throw e;
        }
    }

    // 수정
    public UserResponse updateUser(UUID userId, UserUpdateRequest req) {
        requireNonNull(userId, "userId는 null일 수 없습니다.");
        requireNonNull(req, "요청 본문이 null일 수 없습니다.");
        validateNotBlank(req.name(), "name");
        validateEmail(req.email());
        validateNotBlank(req.password(), "password");

        // 자기 자신을 제외하고 username/email 중복 방지
        ensureUniqueUsernameEmail(req.name(), req.email(), userId);

        try {
            User updated = userService.update(userId, req.name(), req.email(), req.password());
            return UserMapper.toResponse(updated);
        } catch (NoSuchElementException e) {
            log.warn("사용자 수정 실패(존재하지 않음): userId={}", userId);
            throw e;
        } catch (Exception e) {
            log.error("사용자 수정 중 오류: userId={}", userId, e);
            throw e;
        }
    }

    // 삭제
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        try {
            userService.delete(userId);
            log.info("사용자 삭제 완료: userId={}", userId);
        } catch (NoSuchElementException e) {
            log.warn("사용자 삭제 실패(존재하지 않음): userId={}", userId);
            throw e;
        } catch (Exception e) {
            log.error("사용자 삭제 중 오류: userId={}", userId, e);
            throw e;
        }
    }
}