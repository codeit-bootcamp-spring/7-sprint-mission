package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.userStatusDto.UserStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResponseDto createUserStatus(UserStatusRequestDto requestDto);

    UserStatusResponseDto findStatusById(UUID id);

    UserStatusResponseDto updateStatusByUserId(UUID userId);

    List<UserStatusResponseDto> findAllStatus();

    UserStatusResponseDto updateStatus(UserStatusUpdateDto updateDto);

    void deleteUserStatusById(UUID id);
}
