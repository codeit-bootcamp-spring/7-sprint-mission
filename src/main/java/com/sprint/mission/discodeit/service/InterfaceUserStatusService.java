package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InterfaceUserStatusService extends BaseInterfaceService {
    Res_UserStatus create(UUID userID);
    Res_UserStatus find(UUID statusID);
    List<Res_UserStatus> findAll();
    void update(Dto_UserStatus dto);
    Res_UserStatus updateUserStatus(UUID userId, Instant newLastActiveAt);
//    void updateByUserID(UUID userID, Dto_UserCreate dtoUser, String<binary> profileImage);
    void delete(UUID statusID);
}
