package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
import java.util.List;
import java.util.UUID;

public interface InterfaceReadStatusService  {
    void delete(UUID statusID);
    Res_ReadStatus update(UUID readStatusId, Dto_ReadStatusUpdate requestDto);
    List<Res_ReadStatus> findAllByUserId(UUID userID);
    Res_ReadStatus find(UUID statusID);
    Res_ReadStatus create(Dto_ReadStatus dtoReadStatus);
}
