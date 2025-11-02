package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface InterfaceMessageRepository extends BaseInterfaceRepository<Message> {
    Optional<List<Message>> findAllMessageInChannel(UUID channelID);
    Set<UUID> findAllUsersInChannel(List<Message> allMessageInChannel);
}
