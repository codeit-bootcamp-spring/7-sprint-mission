package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    ReadStatus findById(UUID statusId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    ReadStatus delete(UUID statusId);
}
