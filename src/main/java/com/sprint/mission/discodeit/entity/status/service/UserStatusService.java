package com.sprint.mission.discodeit.entity.status.service;

import com.sprint.mission.discodeit.entity.status.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.status.dto.UserStatusInfoDto;
import com.sprint.mission.discodeit.entity.status.dto.UserStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {

    UserStatusInfoDto createUserStatus(UserStatusCreateDto createDto);

    Optional<UserStatusInfoDto> findStatusById(UUID id);

    List<UserStatusInfoDto> findAllStatus();

    Optional<UserStatusInfoDto> updateStatus(UserStatusUpdateDto updateDto);

    void deleteUserStatusById(UUID id);
}
