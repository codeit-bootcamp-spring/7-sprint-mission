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
        if (!userRepository.isExist(request.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_FOR_STATUS);
        } else if (userStatusRepository.isExist(request.getUserId())) {
            throw new CustomException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
        }

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
    public void update(UpdateUserStatusRequestDto request) {
        UserStatus newUserStatus = userStatusRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_STATUS_NOT_FOUND));
        newUserStatus.setUpdatedAt(); // 로그인 시간 변경
        userStatusRepository.update(newUserStatus);
    }

    @Override
    public void updateByUserId(UUID userId) {
        userStatusRepository.findAll().stream()
                .filter(us -> userId.equals(us.getUserId()))
                .findFirst()
                .ifPresentOrElse(us -> {
                    us.setUpdatedAt();
                    userStatusRepository.update(us);
                },
                () -> {
                    throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
                });
    }

    @Override
    public void delete(UUID userStatusId) {
        if(!userStatusRepository.isExist(userStatusId)){
            throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        };
        userStatusRepository.deleteById(userStatusId);
    }
}
