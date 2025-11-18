package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        UUID userId = Objects.requireNonNull(userStatusCreateRequestDto.userId());

        if(userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new IllegalStateException("UserStatus already exists");
        }

        Instant statusAt = userStatusCreateRequestDto.lastReadAt() == null
                ? Instant.now() : userStatusCreateRequestDto.lastReadAt();

        UserStatus userStatus = new UserStatus(
                userId
        );
        userStatus.setLastReadAt(statusAt);

        UserStatus save = userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(save);
    }

    @Override
    public UserStatusResponseDto update(UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UUID id = Objects.requireNonNull(userStatusUpdateRequestDto.id());
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if (userStatusUpdateRequestDto.lastReadAt() != null
                && (userStatus.getLastReadAt() == null || userStatusUpdateRequestDto.lastReadAt().isAfter(userStatus.getLastReadAt()))) {
            userStatus.setLastReadAt(userStatusUpdateRequestDto.lastReadAt());
        }

        UserStatus save = userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(save);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if(userStatusUpdateByUserIdRequestDto.newLastActiveAt() != null
                && (userStatusUpdateByUserIdRequestDto.newLastActiveAt().isAfter(Objects.requireNonNull(userStatus.getLastReadAt())))) {
            userStatus.setLastReadAt(userStatusUpdateByUserIdRequestDto.newLastActiveAt());
        }
        UserStatus save = userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(save);
    }

    @Override
    public UserStatusResponseDto get(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> getAll() {
        List<UserStatus> all = userStatusRepository.findAll();

        return all.stream().map(userStatus -> UserStatusResponseDto.from(userStatus))
                .toList();
    }

    @Override
    public boolean delete(UUID id) {
        return userStatusRepository.deleteById(Objects.requireNonNull(id));
    }
}
