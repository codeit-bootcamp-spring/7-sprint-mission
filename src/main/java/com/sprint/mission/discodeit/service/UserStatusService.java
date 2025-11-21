package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus createUserStatus(UserStatusCreateRequest requestDto);

    UserStatusDto findStatusById(UUID id);

    UserStatus updateStatusByUserId(UUID userId, UserStatusUpdateRequest requestDto);

    UserStatus updateStatusById(UUID id, UserStatusUpdateRequest updateDto);

    List<UserStatusDto> findAllStatus();

    void deleteUserStatusById(UUID id);
}
