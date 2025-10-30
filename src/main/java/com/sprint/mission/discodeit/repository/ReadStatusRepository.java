package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {

    void save(ReadStatus readStatus);

    ReadStatus findById(UUID id);

    ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAll();

    void deleteById(UUID id);

    void deleteByChannelId(UUID id);

    void deleteByChannelMember(UUID channelId, UUID memberId);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}
