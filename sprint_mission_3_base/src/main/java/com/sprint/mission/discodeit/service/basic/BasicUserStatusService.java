package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        UUID userId = request.userId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User with id " + userId + " not found"));

        Optional.ofNullable(user.getStatus())
                .ifPresent(status -> {
                    throw new IllegalArgumentException(
                            "UserStatus with userId " + userId + " already exists");
                });

        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(user, lastActiveAt);
        userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "UserStatus with userId " + userId + " not found"));

        userStatus.update(newLastActiveAt);
        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException(
                    "UserStatus with id " + userStatusId + " not found");
        }
        userStatusRepository.deleteById(userStatusId);
    }

    @Override
    public UserStatusDto getByUserId(UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<UserStatusDto> getAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public UserStatusDto getById(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
