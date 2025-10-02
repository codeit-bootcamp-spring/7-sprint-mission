package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.DTO.MessageRoomDTO;


import java.util.UUID;

public interface MessageRoomService extends BaseService<MessageRoom>{
    void update(UUID id, MessageRoomDTO messageRoomDTO);

}
