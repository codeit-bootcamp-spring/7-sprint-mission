package com.sprint.mission.discodeit.channel.infrastructure;


import com.sprint.mission.discodeit.channel.application.ChannelRepository;
import com.sprint.mission.discodeit.channel.domain.Channel;
import org.springframework.stereotype.Repository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
@Repository
public class FileChannelRepository implements ChannelRepository {

    private final String FILE_PATH = "data/messageRooms.ser"; // 저장 파일 경로


    private Map<UUID, Channel> load() {
        Path filePath = Path.of(FILE_PATH);

        if (Files.notExists(filePath)) {
                return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    private void saveToFile(Map<UUID, Channel> data) {
        Path filePath = Path.of(FILE_PATH);

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Channel channel){
        Map<UUID, Channel> store= load();
        UUID key = channel.getId();
        store.put(key, channel);
        saveToFile(store);
    }

    public void remove(Channel channel){
        Map<UUID, Channel> store= load();
        UUID key = channel.getId();
        store.remove(key);
        saveToFile(store);
    }

    public Optional<Channel> findById(UUID id){
        Map<UUID, Channel> store= load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> store= load();
        return List.copyOf(store.values());
    }

}
