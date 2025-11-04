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
        if (all == null) all = new ArrayList<>(); // ✅ null 방지

        boolean duplName = all.stream()
                .filter(Objects::nonNull)
                .anyMatch(u -> Objects.equals(u.getUsername(), username));
        if (duplName) throw new IllegalArgumentException("username already exists: " + username);

        boolean duplEmail = all.stream()
                .filter(Objects::nonNull)
                .anyMatch(u -> Objects.equals(u.getEmail(), email));
        if (duplEmail) throw new IllegalArgumentException("email already exists: " + email);
    }

    private void ensureUniqueOnUpdate(UUID targetId, String username, String email) {
        var all = userRepository.findAll();
        if (all == null) all = new ArrayList<>();

        boolean duplName = all.stream()
                .filter(Objects::nonNull)
                .anyMatch(u -> !u.getId().equals(targetId) && Objects.equals(u.getUsername(), username));
        if (duplName) throw new IllegalArgumentException("username already exists: " + username);

        boolean duplEmail = all.stream()
                .filter(Objects::nonNull)
                .anyMatch(u -> !u.getId().equals(targetId) && Objects.equals(u.getEmail(), email));
        if (duplEmail) throw new IllegalArgumentException("email already exists: " + email);
    }

    private boolean isOnline(UUID userId) {
        if (userId == null) return false;
        return userStatusRepository.findByUserId(userId)
                .map(UserStatus::getLastSeenAt)
                .map(last -> !last.isBefore(Instant.now().minus(ONLINE_WINDOW)))
                .orElse(false);
    }

    private UUID findProfileImageId(UUID userId) {
        if (userId == null) return null;
        return binaryContentRepository.findByUserId(userId)
                .map(BinaryContent::getId)
                .orElse(null);
    }

    private static UserDto toDto(User u, boolean online, UUID profileImageId) {
        return new UserDto(
                u.getId(),
                u.getCreatedAt(),
                u.getUpdatedAt(),
                u.getUsername(),
                u.getEmail(),
                profileImageId,
                online
        );
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
            var bin = BinaryContent.forUserProfile(user.getId(), a.filename(), a.contentType(), a.data());
            binaryContentRepository.save(bin);
        }

        return toDto(user, true, findProfileImageId(user.getId()));
    }

    @Override
    public Optional<UserDto> find(UUID userId) {
        return userRepository.findById(userId)
                .filter(Objects::nonNull)
                .map(u -> toDto(u, isOnline(u.getId()), findProfileImageId(u.getId())));
    }

    @Override
    public List<UserDto> findAll() {
        var all = userRepository.findAll();
        if (all == null) all = new ArrayList<>();

        // ✅ null 사용자나 id 없는 사용자 제거
        var safeUsers = all.stream()
                .filter(Objects::nonNull)
                .filter(u -> u.getId() != null)
                .toList();

        // ✅ 온라인 상태 매핑
        var statusByUser = new HashMap<UUID, Boolean>();
        for (User u : safeUsers) {
            try {
                boolean status = isOnline(u.getId());
                statusByUser.put(u.getId(), status);
            } catch (Exception e) {
                statusByUser.put(u.getId(), false);
            }
        }

        // ✅ 프로필 이미지 매핑
        var profileIdByUser = new HashMap<UUID, UUID>();
        for (User u : safeUsers) {
            try {
                UUID profileId = findProfileImageId(u.getId());
                profileIdByUser.put(u.getId(), profileId);
            } catch (Exception e) {
                profileIdByUser.put(u.getId(), null);
            }
        }

        // ✅ 최종 변환
        return safeUsers.stream()
                .map(u -> toDto(
                        u,
                        statusByUser.getOrDefault(u.getId(), false),
                        profileIdByUser.getOrDefault(u.getId(), null)
                ))
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
            binaryContentRepository.deleteByUserId(user.getId());
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
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.findByUserId(userId)
                .ifPresent(s -> userStatusRepository.deleteById(s.getId()));
        if (readStatusRepository != null) {
            readStatusRepository.deleteAllByUserId(userId);
        }

        // 2) 본체 삭제
        userRepository.deleteById(userId);
    }
}
