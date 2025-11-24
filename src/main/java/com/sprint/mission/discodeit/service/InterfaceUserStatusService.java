package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.dto.Res_UserUpdate;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InterfaceUserStatusService  {
    Res_UserUpdate create(UUID userID);
    Res_UserUpdate find(UUID statusID);
    List<Res_UserUpdate> findAll();
    void update(Dto_UserStatus dto);
    Res_UserUpdate updateUserStatus(UUID userId, Instant newLastActiveAt);
//    void updateByUserID(UUID userID, UserCreateRequest dtoUser, String<binary> profileImage);
    void delete(UUID statusID);
}
