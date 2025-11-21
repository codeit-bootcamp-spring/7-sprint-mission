package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.ReadStatusCteateRequest;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.ReadStatusDto;
import java.util.List;
import java.util.UUID;

public interface InterfaceReadStatusService  {
    void delete(UUID statusID);
    ReadStatusDto update(UUID readStatusId, Dto_ReadStatusUpdate requestDto);
    List<ReadStatusDto> findAllByUserId(UUID userID);
    ReadStatusDto find(UUID statusID);
    ReadStatusDto create(ReadStatusCteateRequest dtoReadStatus);
}
