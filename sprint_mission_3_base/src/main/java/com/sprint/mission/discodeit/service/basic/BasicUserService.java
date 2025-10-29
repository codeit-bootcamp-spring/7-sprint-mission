package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    // (선택) 유저 삭제 시 ReadStatus도 정리하려면 주입
    private final ReadStatusRepository readStatusRepository = null; // 필요 없으면 null 유지

    private static final Duration ONLINE_WINDOW = Duration.ofMinutes(5);

    /* ---------- 유틸 ---------- */

    private void ensureUniqueOnCreate(String username, String email) {
        var all = userRepository.findAll();
        boolean duplName = all.stream().anyMatch(u -> Objects.equals(u.getUsername(), username));
        if (duplName) throw new IllegalArgumentException("username already exists: " + username);
        boolean duplEmail = all.stream().anyMatch(u -> Objects.equals(u.getEmail(), email));
        if (duplEmail) throw new IllegalArgumentException("email already exists: " + email);
    }

    private void ensureUniqueOnUpdate(UUID targetId, String username, String email) {
        var all = userRepository.findAll();
        boolean duplName = all.stream()
                .anyMatch(u -> !u.getId().equals(targetId) && Objects.equals(u.getUsername(), username));
        if (duplName) throw new IllegalArgumentException("username already exists: " + username);
        boolean duplEmail = all.stream()
                .anyMatch(u -> !u.getId().equals(targetId) && Objects.equals(u.getEmail(), email));
        if (duplEmail) throw new IllegalArgumentException("email already exists: " + email);
    }

    private boolean isOnline(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .map(UserStatus::getLastSeenAt)
                .map(last -> !last.isBefore(Instant.now().minus(ONLINE_WINDOW)))
                .orElse(false);
    }

    private UUID findProfileImageId(UUID userId) {
        return binaryContentRepository.findByUserId(userId)
                .map(BinaryContent::getId)
                .orElse(null);
    }

    private static UserDto toDto(User u, boolean online, UUID profileImageId) {
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), online, profileImageId);
    }

    /* ---------- 구현 ---------- */

    @Override
    public UserDto create(UserCreateRequest request) {
        Objects.requireNonNull(request.username(), "username");
        Objects.requireNonNull(request.email(), "email");
        Objects.requireNonNull(request.password(), "password");

        ensureUniqueOnCreate(request.username(), request.email());

        // 1) 사용자 생성
        User user = new User(request.username(), request.email(), request.password());
        userRepository.save(user);

        // 2) UserStatus 생성(now)
        userStatusRepository.save(new UserStatus(user.getId(), Instant.now()));

        // 3) (선택) 프로필 이미지 저장
        if (request.profileImage() != null) {
            var a = request.profileImage();
            // BinaryContent.forUserProfile(...) 정적 팩토리가 없는 경우엔, 프로젝트의 BinaryContent 생성자에 맞춰 바꿔주세요.
            var bin = BinaryContent.forUserProfile(user.getId(), a.filename(), a.contentType(), a.data());
            binaryContentRepository.save(bin);
        }

        return toDto(user, true, findProfileImageId(user.getId()));
    }

    @Override
    public Optional<UserDto> find(UUID userId) {
        return userRepository.findById(userId)
                .map(u -> toDto(u, isOnline(u.getId()), findProfileImageId(u.getId())));
    }

    @Override
    public List<UserDto> findAll() {
        var all = userRepository.findAll();
        var statusByUser = all.stream().collect(Collectors.toMap(
                User::getId, u -> isOnline(u.getId())
        ));
        var profileIdByUser = all.stream().collect(Collectors.toMap(
                User::getId, u -> findProfileImageId(u.getId())
        ));
        return all.stream()
                .map(u -> toDto(u, statusByUser.get(u.getId()), profileIdByUser.get(u.getId())))
                .toList();
    }

    @Override
    public UserDto update(UserUpdateRequest request) {
        Objects.requireNonNull(request.id(), "id");
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + request.id()));

        // username/email 바뀌려면 유일성 체크
        String nextName = request.username() != null ? request.username() : user.getUsername();
        String nextEmail = request.email() != null ? request.email() : user.getEmail();
        ensureUniqueOnUpdate(user.getId(), nextName, nextEmail);

        // 1) 본문 업데이트
        user.update(request.username(), request.email(), request.password());
        userRepository.save(user);

        // 2) (선택) 프로필 이미지 교체
        if (request.profileImage() != null) {
            binaryContentRepository.deleteByUserId(user.getId()); // 기존 이미지 제거
            var a = request.profileImage();
            var bin = BinaryContent.forUserProfile(user.getId(), a.filename(), a.contentType(), a.data());
            binaryContentRepository.save(bin);
        }

        return toDto(user, isOnline(user.getId()), findProfileImageId(user.getId()));
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found: " + userId);
        }
        // 1) 관련 리소스 정리
        binaryContentRepository.deleteByUserId(userId);     // 프로필 이미지 제거
        userStatusRepository.findByUserId(userId).ifPresent(s -> userStatusRepository.deleteById(s.getId()));
        if (readStatusRepository != null) {
            readStatusRepository.deleteAllByUserId(userId);
        }
        // 2) 본체 삭제
        userRepository.deleteById(userId);
    }
}
