package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.converter.UserStatusDtoConverter;
import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void create(CreateUserStatusRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_FOR_STATUS));

        userStatusRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_ALREADY_EXISTS));

        UserStatus newUserStatus = new UserStatus(request.userId(), user.getCreatedAt());
        userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusRepository.findAll());
    }

    @Override
    public UserStatusResponseDto update(UUID userStatusId, UpdateUserStatusRequestDto request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.update(newLastActiveAt);
        userStatusRepository.update(userStatus);
        return toDto(userStatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UpdateUserStatusRequestDto request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.update(newLastActiveAt);
        userStatusRepository.update(userStatus);
        return toDto(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.findById(userStatusId)
                .orElseThrow(() ->  new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

        userStatusRepository.deleteById(userStatusId);
    }

    private UserStatusResponseDto toDto(UserStatus userStatus) {
        return UserStatusDtoConverter.toResponseDto(userStatus);
    }
}
