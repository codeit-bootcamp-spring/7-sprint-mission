package com.sprint.mission.discodeit.repository.Memory;

import com.sprint.mission.discodeit.domain.MessageRoom;
import com.sprint.mission.discodeit.application.repository.MessageRoomRepository;

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
