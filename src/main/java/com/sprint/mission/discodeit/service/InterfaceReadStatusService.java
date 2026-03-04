package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.dto_Neo.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.dto_Neo.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.dto_Neo.ReadStatusDto;
import java.util.List;
import java.util.UUID;

public interface InterfaceReadStatusService  {
    void delete(UUID statusID);
    ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest requestDto);
    List<ReadStatusDto> findAllByUserId(UUID userID);
    ReadStatusDto find(UUID statusID);
    ReadStatusDto create(ReadStatusCreateRequest dtoReadStatus);
}
