package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.userStatus.UserStatusDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    public UserStatusDto createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto);
    public void deleteUserStatus(UUID userStatusId);
    public void updateByUserId(UUID userId);
    public List<UserStatusDto> findAll();
    UserStatusDto patchUserStatus(UUID userId, UserStatusPatchRequestDto dto);


}
