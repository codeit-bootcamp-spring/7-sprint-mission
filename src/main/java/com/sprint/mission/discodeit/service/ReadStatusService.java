package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    void update(UUID id);
    void delete(UUID id);
    List<ReadStatusInfoRes> findAllByChannelId(UUID channelId);
    List<ReadStatusInfoRes> findAllByUserId(UUID userId);
}
