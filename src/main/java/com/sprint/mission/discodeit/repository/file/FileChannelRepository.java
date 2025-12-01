/*
package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileChannelRepository implements ChannelRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path CHANNELS_FILE = DATA_DIR.resolve("channels.ser");
    private final Map<UUID, Channel> cache = new ConcurrentHashMap<>();

    public FileChannelRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(CHANNELS_FILE)) {
                loadAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(CHANNELS_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw) {
                cache.clear();
                for(Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getValue() instanceof Channel && entry.getKey() instanceof UUID) {
                        cache.put((UUID) entry.getKey(), (Channel) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid channel entry: " + entry.getValue().getClass());
                    }
                }
                return;
            }
            throw new IllegalStateException("Unexpected object type: " + obj.getClass());
        } catch (EOFException e) {
            cache.clear();
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll() {
        try {
            Path tmp = CHANNELS_FILE.resolveSibling("channels.ser.tmp");
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                oos.writeObject(cache);
            }
            try {
                Files.move(tmp, CHANNELS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, CHANNELS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        Objects.requireNonNull(channel, "channel cannot be null");
        cache.put(channel.getId(), channel);
        saveAll();
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Objects.requireNonNull(id , "id cannot be null");
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        Objects.requireNonNull(id);
        boolean existed = (cache.remove(id) != null);
        if(existed) {
            saveAll();
        }
        return existed;
    }

    @Override
    public List<Channel> findAll() {
        return cache.values().stream().toList();
    }
}

 */
