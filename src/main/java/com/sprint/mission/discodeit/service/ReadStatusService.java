package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusDto create(ReadStatusCreateRequest request);

    ReadStatusDto find(UUID readStatusId);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatusDto update(UUID readStatusId, ReadStatusUpdateReuqest request);

    void delete(UUID uuid);


}
