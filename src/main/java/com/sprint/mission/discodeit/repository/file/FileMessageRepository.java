package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String filePath;
    private Map<java.util.UUID, Message> data;

    public FileMessageRepository(String filePath) {
        this.filePath = filePath;
        this.data = load();
    }

    @SuppressWarnings("unchecked")
    private Map<java.util.UUID, Message> load() {
        File f = new File(filePath);
        if (!f.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<java.util.UUID, Message>) ois.readObject();
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
    public void save(Message message) {
        data.put(message.getId(), message);
        persist();
    }

    @Override
    public Message findById(java.util.UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(java.util.UUID id) {
        data.remove(id);
        persist();
    }
}