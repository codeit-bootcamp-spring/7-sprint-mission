package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BaseInterfaceRepository<T> {
    void save(T model);
    boolean deleteById(UUID id);
    Optional<T> findById(UUID id);
    List<T> findAll();

    default Optional<UserStatus> findByUserId(UUID userID) {
        return Optional.empty();
    }
    default Optional<List<Message>> findAllMessageInChannel(UUID channelID) {
        return Optional.empty();
    }
    default Set<UUID> findAllUsersInChannel(List<Message> allMessageInChannel) {
        return new HashSet<>();
    }

    default Optional<ReadStatus> findByUserAndChannelId(UUID userID, UUID channelID) {
        return Optional.empty();
    }
}
