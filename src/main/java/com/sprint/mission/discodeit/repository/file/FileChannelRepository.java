package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String filePath;
    private Map<java.util.UUID, Channel> data;

    public FileChannelRepository(String filePath) {
        this.filePath = filePath;
        this.data = load();
    }

    @SuppressWarnings("unchecked")
    private Map<java.util.UUID, Channel> load() {
        File f = new File(filePath);
        if (!f.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<java.util.UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void persist() {
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        persist();
    }

    @Override
    public Channel findById(java.util.UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(java.util.UUID id) {
        data.remove(id);
        persist();
    }
}