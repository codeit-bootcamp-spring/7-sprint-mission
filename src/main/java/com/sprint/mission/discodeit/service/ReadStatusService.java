package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindByUserRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

     ReadStatusResponse create(ReadStatusCreateRequest request);
    ReadStatusResponse find(ReadStatusFindRequest request);
     List<ReadStatusResponse> findAllByUserId(ReadStatusFindByUserRequest request);
    ReadStatusResponse update(ReadStatusUpdateReuqest request);
     void delete(UUID uuid);

}
