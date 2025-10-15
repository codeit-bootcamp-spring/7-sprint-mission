package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.Channel;

import com.sprint.mission.discodeit.application.repository.ChannelRepository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final String FILE_PATH = "data/channels.ser"; // 저장 파일 경로

    // 유저 데이터 전체를 파일에서 불러오기
    private Map<UUID, Channel> load() {

        Path filePath = Path.of(FILE_PATH);


        try {
            if (Files.notExists(filePath)) {
                return new HashMap<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, Channel>) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, Channel> data) {
        Path filePath = Path.of(FILE_PATH);
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Channel channel){
        Map<UUID, Channel> store = load();
        UUID key = channel.getId();
        store.put(key, channel);
        saveToFile(store);
    }
    public void remove(Channel channel){
        Map<UUID, Channel> store = load();
        UUID findChannelId = channel.getId();
        store.remove(findChannelId);
        saveToFile(store);
    }

    public Optional<Channel> findById(UUID id){
        Map<UUID, Channel> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> store = load();
        return List.copyOf(store.values().stream().toList());
    }

}
