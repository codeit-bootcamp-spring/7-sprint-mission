package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
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
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_FOR_STATUS));

        userStatusRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_ALREADY_EXISTS));

        UserStatus newUserStatus = new UserStatus(request.getUserId());
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
    public UserStatus update(UUID userStatusId, UpdateUserStatusRequestDto request) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.setUpdatedAt(); // 로그인 시간 변경
        userStatusRepository.update(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UpdateUserStatusRequestDto request) {
        Instant newLastActiveAt = request.getNewLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.update(newLastActiveAt);
        userStatusRepository.update(userStatus);
        return userStatus;
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.findById(userStatusId)
                .orElseThrow(() ->  new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));

        userStatusRepository.deleteById(userStatusId);
    }
}
