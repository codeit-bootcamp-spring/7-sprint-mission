package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends Repository<Message, UUID> {

    // 공통 CRUD 메서드는 Repository에서 상속받음
    List<Message> findByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);
}
