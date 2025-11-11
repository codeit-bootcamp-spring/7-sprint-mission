package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
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
    public UserStatus create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
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

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UUID id = Objects.requireNonNull(userStatusUpdateRequestDto.id());
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if (userStatusUpdateRequestDto.lastReadAt() != null
                && (userStatus.getLastReadAt() == null || userStatusUpdateRequestDto.lastReadAt().isAfter(userStatus.getLastReadAt()))) {
            userStatus.setLastReadAt(userStatusUpdateRequestDto.lastReadAt());
        }

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        UUID userId = Objects.requireNonNull(userStatusUpdateByUserIdRequestDto.userId());
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if(userStatusUpdateByUserIdRequestDto.lastReadAt() != null
                && (userStatus.getLastReadAt() == null || userStatusUpdateByUserIdRequestDto.lastReadAt().isAfter(userStatus.getLastReadAt()))) {
            userStatus.setLastReadAt(userStatusUpdateByUserIdRequestDto.lastReadAt());
        }
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus get(UUID id) {
        return userStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
    }

    @Override
    public List<UserStatus> getAll() {
        return  userStatusRepository.findAll();
    }

    @Override
    public boolean delete(UUID id) {
        return userStatusRepository.deleteById(Objects.requireNonNull(id));
    }
}
