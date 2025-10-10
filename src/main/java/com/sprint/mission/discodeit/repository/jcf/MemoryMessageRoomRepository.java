package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.DTO.MessageRoomDTO;
import com.sprint.mission.discodeit.entity.MessageRoomType;
import com.sprint.mission.discodeit.repository.MessageRoomRepository;

import java.util.*;

public class MemoryMessageRoomRepository implements MessageRoomRepository {

    private final Map<UUID, MessageRoom> store= new HashMap<>();

    public void save(MessageRoom messageRoom){
        UUID key = messageRoom.getId();
        store.put(key,messageRoom);
    }

    public void remove(MessageRoom messageRoom){
        UUID key = messageRoom.getId();
        store.remove(key);
    }

    public Optional<MessageRoom> findById(UUID id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<MessageRoom> findAll() {
        return List.copyOf(store.values().stream().toList());
    }

}
