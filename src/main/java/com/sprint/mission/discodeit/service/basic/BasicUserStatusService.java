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
            throw new NoSuchElementException("User not found");
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new IllegalStateException("UserStatus already exists");
        }

        Instant statusAt = userStatusCreateRequestDto.lastReadAt() == null
                ? Instant.now() : userStatusCreateRequestDto.lastReadAt();

        UserStatus userStatus = UserStatus.builder()
                .userId(userId)
                .build();
        userStatus.setLastReadAt(statusAt);

        UserStatus save = userStatusRepository.save(userStatus);


        return new UserStatusResponseDto(
                save.getId(),
                save.getUserId(),
                save.getLastReadAt(),
                save.isOnlineNow()
        );
    }

    @Override
    public UserStatusResponseDto update(UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UUID id = Objects.requireNonNull(userStatusUpdateRequestDto.id());
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));

        if (userStatusUpdateRequestDto.lastReadAt() != null
                && (userStatus.getLastReadAt() == null || userStatusUpdateRequestDto.lastReadAt().isAfter(userStatus.getLastReadAt()))) {
            userStatus.setLastReadAt(userStatusUpdateRequestDto.lastReadAt());
        }

        UserStatus save = userStatusRepository.save(userStatus);

        return new UserStatusResponseDto(
                save.getId(),
                save.getUserId(),
                save.getLastReadAt(),
                save.isOnlineNow()
        );
    }

    @Override
    public UserStatusResponseDto updateByUserId(UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        UUID userId = Objects.requireNonNull(userStatusUpdateByUserIdRequestDto.userId());
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));

        if(userStatusUpdateByUserIdRequestDto.lastReadAt() != null
                && (userStatus.getLastReadAt() == null || userStatusUpdateByUserIdRequestDto.lastReadAt().isAfter(userStatus.getLastReadAt()))) {
            userStatus.setLastReadAt(userStatusUpdateByUserIdRequestDto.lastReadAt());
        }
        UserStatus save = userStatusRepository.save(userStatus);
        return new UserStatusResponseDto(
                save.getId(),
                save.getUserId(),
                save.getLastReadAt(),
                save.isOnlineNow()
        );
    }

    @Override
    public UserStatusResponseDto get(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastReadAt(),
                userStatus.isOnlineNow()
        );
    }

    @Override
    public List<UserStatusResponseDto> getAll() {
        return  userStatusRepository.findAll().stream()
                .map(rs -> new UserStatusResponseDto(
                        rs.getId(),
                        rs.getUserId(),
                        rs.getLastReadAt(),
                        rs.isOnlineNow()
                ))
                .toList();
    }

    @Override
    public boolean delete(UUID id) {
        return userStatusRepository.deleteById(Objects.requireNonNull(id));
    }
}
