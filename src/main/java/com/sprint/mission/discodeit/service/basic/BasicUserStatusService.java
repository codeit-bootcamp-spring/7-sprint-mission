package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserStatus create(UserStatusCreateRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalStateException("해당 유저가 없습니다: " + request.userId())
                );

        if (user.getStatus() != null) {
            throw new IllegalStateException("이미 UserStatus가 존재합니다. userId: " + request.userId());
        }

        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(user, lastActiveAt);

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {

        return userStatusRepository
                .findByUserId(userStatusId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userStatusId));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream()
                .toList();

    }


    @Override
    public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        userStatus.update(newLastActiveAt);

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();
        UserStatus byUserId = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        byUserId.update(newLastActiveAt);

        return userStatusRepository.save(byUserId);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.findAll().stream().anyMatch(userStatus -> userStatus.getId().equals(userStatusId))) {
            throw new NoSuchElementException("유저스타터스아이디못찾아" + userStatusId + "못찾아 ");
        }

        userStatusRepository.deleteByUserId(userStatusId);
    }
}
