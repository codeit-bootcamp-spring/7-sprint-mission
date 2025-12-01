package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        UUID userId = Objects.requireNonNull(userStatusCreateRequestDto.userId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new IllegalStateException("UserStatus already exists");
        }

        Instant statusAt = userStatusCreateRequestDto.lastActiveAt() == null
                ? Instant.now() : userStatusCreateRequestDto.lastActiveAt();

        UserStatus userStatus = new UserStatus(user);
        userStatus.setLastActiveAt(statusAt);

        UserStatus save = userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public UserStatusResponseDto update(UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UUID id = Objects.requireNonNull(userStatusUpdateRequestDto.id());
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if (userStatusUpdateRequestDto.lastActiveAt() != null
                && (userStatus.getLastActiveAt() == null
                || userStatusUpdateRequestDto.lastActiveAt().isAfter(userStatus.getLastActiveAt()))) {
            userStatus.setLastActiveAt(userStatusUpdateRequestDto.lastActiveAt());
        }

        UserStatus save = userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        if(userStatusUpdateByUserIdRequestDto.newLastActiveAt() != null
                && (userStatusUpdateByUserIdRequestDto.newLastActiveAt()
                .isAfter(Objects.requireNonNull(userStatus.getLastActiveAt())))) {
            userStatus.setLastActiveAt(userStatusUpdateByUserIdRequestDto.newLastActiveAt());
        }
        UserStatus save = userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(save);
    }

    @Override
    public UserStatusResponseDto get(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> getAll() {
        List<UserStatus> all = userStatusRepository.findAll();

        return all.stream().map(userStatus -> userStatusMapper.toDto(userStatus))
                .toList();
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        if(!userStatusRepository.existsByUserId(id)) {
            return false;
        }
        userStatusRepository.deleteById(Objects.requireNonNull(id));
        return true;
    }
}
