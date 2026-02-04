package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InterfaceUserStatusService  {
    UserStatusDto create(UUID userID);
    UserStatusDto find(UUID statusID);
    List<UserStatusDto> findAll();
    void update(Dto_UserStatus dto);
    UserStatusDto updateUserStatus(UUID userId, Instant newLastActiveAt);
//    void updateByUserID(UUID userID, UserCreateRequest dtoUser, String<binary> profileImage);
    void delete(UUID statusID);
}
