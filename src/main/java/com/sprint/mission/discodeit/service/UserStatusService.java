package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.request.CreateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusResponseDto createUserStatus(CreateUserStatusRequestDto requestDto);

  UserStatusResponseDto find(UUID userStatusId);

  List<UserStatusResponseDto> findAll();

  UserStatus updateUserStatus(UUID userId);

  UserStatus updateByUserId(UUID userId);

  void deleteUserStatus(UUID userStatusId);
}
