package com.sprint.mission.repository.file;

import com.sprint.mission.config.DataPath;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.ChannelAlreadyExistsException;
import com.sprint.mission.exceptions.ChannelNotFoundException;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.jcf.JCFChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = DataPath.FILE_DIR + "/channel.sav";
    private static final FileChannelRepository instance = new FileChannelRepository();
    private static final Map<UUID, Channel> data = new HashMap<>();

    private FileChannelRepository(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            List<Channel> objects = (List<Channel>) ois.readObject();
            for (Channel object : objects) {
                data.put(object.getUuid(), object);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileChannelRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Channel channel) {
        if(existsById(channel.getUuid()))
            throw new ChannelAlreadyExistsException("채널 업데이트는 update를 사용해주세요.");
        data.put(channel.getUuid(), channel);
        write();
    }

    @Override
    public void update(Channel channel) {
        if (!existsById(channel.getUuid())) {
            throw new ChannelNotFoundException(channel.getUuid());
        }
        data.put(channel.getUuid(), channel);
        write();
    }


    @Override
    public Channel findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(Channel::getDisplayName))
                .toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        if (!existsById(uuid)) {
            throw new ChannelNotFoundException(uuid);
        }
        data.remove(uuid);
        write();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return data.containsKey(uuid);
    }

    @Override
    public boolean existsByName(String name) {
        return data.values().stream()
                .anyMatch(c -> c.getDisplayName().equals(name));
    }

    private void write(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
