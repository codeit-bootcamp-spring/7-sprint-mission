package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto);
    UserStatusResponseDto update(UserStatusUpdateRequestDto userStatusUpdateRequestDto);
    UserStatusResponseDto updateByUserId(UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto);
    UserStatusResponseDto get(UUID id);
    List<UserStatusResponseDto> getAll();
    boolean delete(UUID id);
}
