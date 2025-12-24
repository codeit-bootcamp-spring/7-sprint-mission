package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageQueryRepository {

    List<Message> findAllByChannelId(
            UUID channelId,
            int size,
            String sort,
            Instant cursor);

    Long getTotalElementsByChannelId(UUID channelId);
}
