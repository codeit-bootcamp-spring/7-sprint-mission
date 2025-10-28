package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    public UserStatus createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto);
    public void deleteUserStatus(UUID userStatusId);
    public <T>void updateUserStatus(UserStatusUpdateRequestDto<T> userStatusUpdateRequestDto);
    public void updateByUserId(UUID userId);
    public UserStatus find(UUID userStatusId);
    public List<UserStatus> findAll();

}
