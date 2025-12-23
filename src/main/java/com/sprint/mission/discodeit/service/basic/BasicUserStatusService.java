package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public UserStatusResponseDto createUserStatus(CreateUserStatusDto createUserStatusDto) {
        User user = userRepository.findById(createUserStatusDto.userId())
                .orElseThrow(() -> UserNotFoundException.byId(createUserStatusDto.userId()));

        if (userStatusRepository.findByUserId(createUserStatusDto.userId()).isPresent()) {
            throw UserStatusNotFoundException.byUserId(createUserStatusDto.userId());
        }

        UserStatus userStatus = UserStatus.builder()
                .user(user)
                .lastActiveAt(createUserStatusDto.lastActiveAt())
                .build();

        userStatusRepository.save(userStatus);

        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusResponseDto getUserStatus(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));

        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusResponseDto> getAllUserStatuses() {
        return userStatusRepository.findAll().stream()
                .map(userStatusMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public UserStatusResponseDto updateUserStatus(UUID userStatusId,
                                                  UpdateUserStatusDto updateUserStatusDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));

        userStatus.update(updateUserStatusDto.newLastActiveAt());
        userStatusRepository.save(userStatus);

        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional
    public UserStatusResponseDto updateStatusByUserId(UUID userId,
                                                      UpdateUserStatusDto updateUserStatusDto) {
        Instant lastActiveAt = updateUserStatusDto.newLastActiveAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.byUserId(userId));
        userStatus.update(lastActiveAt);
        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional
    public void deleteById(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.byId(userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
