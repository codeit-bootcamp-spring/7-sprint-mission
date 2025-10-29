package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public interface MessageRepository {
    Message save(Message m);

    Optional<Message> findById(UUID id);

    // ✅ 채널 기준 조회만 제공 (findAll 제거)
    List<Message> findAllByChannelId(UUID channelId);

    void deleteById(UUID id);

    // ✅ 채널 기준 일괄 삭제
    void deleteAllByChannelId(UUID channelId);

    boolean existsById(UUID id);
}
