package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);

    List<Channel> findAllPublic();

    void setParticipants(UUID channelId, Collection<UUID> userIds);

    List<UUID> participantUserIds(UUID channelId);

    List<Channel> findAllPrivateByUserId(UUID userId);
}
