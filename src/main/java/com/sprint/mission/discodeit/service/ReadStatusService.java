package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateReuqest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

     ReadStatus create(ReadStatusCreateReuqest request);
     ReadStatus find(UUID readStatusId);
     List<ReadStatus> findAllByUserId(UUID userId);
     ReadStatus update(ReadStatusUpdateReuqest request);
     void delete(UUID uuid);

}
