package com.sprint.mission.discodeit.messageroom.application;

import com.sprint.mission.discodeit.messageroom.domain.MessageRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRoomRepository {
    void save(MessageRoom messageRoom);

    void remove(MessageRoom messageRoom);

    Optional<MessageRoom> findById(UUID id);

    List<MessageRoom> findAll();
}
