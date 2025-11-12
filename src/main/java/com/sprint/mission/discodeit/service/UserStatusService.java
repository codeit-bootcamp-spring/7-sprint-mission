package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {

    UserStatus createUserStatus(UserStatusRequestDto requestDto);

    UserStatusResponseDto findStatusById(UUID id);

    UserStatus updateStatusByUserId(UUID userId, UserStatusUpdateDto requestDto);

    UserStatus updateStatusById(UUID id, UserStatusUpdateDto updateDto);

    List<UserStatusResponseDto> findAllStatus();

    void deleteUserStatusById(UUID id);
}
