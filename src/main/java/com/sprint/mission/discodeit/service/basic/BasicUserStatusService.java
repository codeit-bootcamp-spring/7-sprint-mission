package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.util.exception.CustomException;
import com.sprint.mission.discodeit.global.util.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    @Override
    public UserStatusResponseDto createUserStatus(CreateUserStatusDto createUserStatusDto) {
        if(!userRepository.existsById(createUserStatusDto.userId())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if(userStatusRepository.findByUserId(createUserStatusDto.userId()).isPresent()) {
            throw new CustomException(ErrorCode.USER_STATUS_ALREADY_EXIST);
        }

        UserStatus userstatus = new UserStatus(createUserStatusDto.userId(), createUserStatusDto.loginAt());
        userStatusRepository.save(userstatus);

        return UserStatusResponseDto.from(userstatus);
    }

    @Override
    public UserStatusResponseDto getUserStatus(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> getAllUserStatuses() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusResponseDto::from)
                .toList();
    }

    @Override
    public UserStatusResponseDto updateUserStatus(UUID userStatusId, UpdateUserStatusDto updateUserStatusDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

        userStatus.update(updateUserStatusDto.loginAt());
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto updateStatusByUserId(UUID userId, UpdateUserStatusDto updateUserStatusDto) {
        Instant lastActiveAt = updateUserStatusDto.loginAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.update(lastActiveAt);
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        if(!userStatusRepository.existsById(userStatusId)) {
            throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
