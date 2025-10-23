package com.sprint.mission.discodeit.messageroom.presentation;


import com.sprint.mission.discodeit.messageroom.domain.MessageRoom;

import java.util.List;
import java.util.UUID;

public interface MessageRoomService {
    void save(MessageRoom messageRoom);

    void remove(MessageRoom messageRoom);

    MessageRoom findById(UUID id);

    List<MessageRoom> findAll();

}
