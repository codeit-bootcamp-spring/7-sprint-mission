package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.global.exception.userstatus.UserNotFoundForUserStatusException;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public void create(CreateUserStatusRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundForUserStatusException(
                        ErrorCode.USER_NOT_FOUND_FOR_USER_STATUS,
                        Map.of("userId", request.userId())
                ));

        userStatusRepository.findById(request.userId())
                .orElseThrow(() -> new UserStatusAlreadyExistsException(
                        ErrorCode.USER_STATUS_ALREADY_EXISTS,
                        Map.of("userId", request.userId())
                ));

        UserStatus newUserStatus = new UserStatus(user);
        userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatusResponseDto find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new UserStatusNotFoundException(
                        ErrorCode.USER_STATUS_NOT_FOUND,
                        Map.of("userStatusId", userStatusId)
                ));

        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        // fetch join 적용
        return new ArrayList<>(userStatusRepository.findAllWithUser()).stream()
                .map(userStatus -> userStatusMapper.toResponseDto(userStatus))
                .toList();
    }

    @Override
    @Transactional
    public UserStatusResponseDto update(UUID userStatusId, UpdateUserStatusRequestDto request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new UserStatusNotFoundException(
                        ErrorCode.USER_STATUS_NOT_FOUND,
                        Map.of("userStatusId", userStatusId)
                ));
        userStatus.update(newLastActiveAt);
        userStatusRepository.save(userStatus);
        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional
    public UserStatusResponseDto updateByUserId(UUID userId, UpdateUserStatusRequestDto request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new UserStatusNotFoundException(
                        ErrorCode.USER_STATUS_NOT_FOUND,
                        Map.of("userId", userId)
                ));
        userStatus.update(newLastActiveAt);
        userStatusRepository.save(userStatus);
        return userStatusMapper.toResponseDto(userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID userStatusId) {
        userStatusRepository.findById(userStatusId)
                .orElseThrow(() ->  new UserStatusNotFoundException(
                        ErrorCode.USER_STATUS_NOT_FOUND,
                        Map.of("userStatusId", userStatusId)
                ));

        userStatusRepository.deleteById(userStatusId);
    }
}
