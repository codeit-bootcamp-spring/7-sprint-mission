package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    void update(UUID id);
    ReadStatus delete(UUID id);
    List<ReadStatus> findAllByChannelId(UUID channelId);
}
