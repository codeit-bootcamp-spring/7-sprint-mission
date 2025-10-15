package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.domain.MessageRoom;
import com.sprint.mission.discodeit.dto.MessageRoomDto;


import java.util.List;
import java.util.UUID;

public interface MessageRoomService {
    void save(MessageRoom messageRoom);

    void remove(MessageRoom messageRoom);

    MessageRoom findById(UUID id);

    List<MessageRoom> findAll();

    void update(UUID id, MessageRoomDto messageRoomDTO);
}
