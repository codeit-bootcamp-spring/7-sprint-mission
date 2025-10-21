package com.sprint.mission.discodeit.messageroom.infrastructure;


import com.sprint.mission.discodeit.messageroom.application.MessageRoomRepository;
import com.sprint.mission.discodeit.messageroom.domain.MessageRoom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageRoomRepository implements MessageRoomRepository {

    private final String FILE_PATH = "data/messageRoom.ser"; // 저장 파일 경로


    private Map<UUID, MessageRoom> load() {
        Path filePath = Path.of(FILE_PATH);
        try {
            if (Files.notExists(filePath)) {
                return new HashMap<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, MessageRoom>) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    private void saveToFile(Map<UUID, MessageRoom> data) {
        Path filePath = Path.of(FILE_PATH);
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(MessageRoom messageRoom){
        Map<UUID, MessageRoom> store= load();
        UUID key = messageRoom.getId();
        store.put(key,messageRoom);
        saveToFile(store);
    }

    public void remove(MessageRoom messageRoom){
        Map<UUID, MessageRoom> store= load();
        UUID key = messageRoom.getId();
        store.remove(key);
        saveToFile(store);
    }

    public Optional<MessageRoom> findById(UUID id){
        Map<UUID, MessageRoom> store= load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<MessageRoom> findAll() {
        Map<UUID, MessageRoom> store= load();
        return List.copyOf(store.values().stream().toList());
    }

}
