package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_UserStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatusByID;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;

import java.util.List;
import java.util.UUID;

public interface InterfaceUserStatusService extends BaseInterfaceService {
    Res_UserStatus create(Dto_UserStatusByID dtoUserStatus);
    Res_UserStatus find(UUID statusID);
    List<Res_UserStatus> findAll();
    void update(Dto_UserStatus dto);
    void updateByUserID(Dto_UserStatusByID dto);
    void delete(UUID statusID);
}
