package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindByUserRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus create(ReadStatusCreateRequest request);
    ReadStatus find(UUID readStatusId);
     List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId,ReadStatusUpdateReuqest request);
     void delete(UUID uuid);

}
