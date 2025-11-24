/*
package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path BINARYCONTENT_FILE = DATA_DIR.resolve("binaries.ser");
    private final Map<UUID, BinaryContent> cache = new ConcurrentHashMap<>();

    public FileBinaryContentRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(BINARYCONTENT_FILE)) {
                loadAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(BINARYCONTENT_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw) {
                cache.clear();
                for(Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getValue() instanceof BinaryContent && entry.getKey() instanceof UUID) {
                        cache.put((UUID) entry.getKey(), (BinaryContent) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid binarycontent entry: " + entry.getValue().getClass());
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
            Path tmp = BINARYCONTENT_FILE.resolveSibling("binaries.ser.tmp");
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                oos.writeObject(cache);
            }
            try {
                Files.move(tmp, BINARYCONTENT_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, BINARYCONTENT_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Objects.requireNonNull(binaryContent, "binaryContent must not be null");
        cache.put(binaryContent.getId(), binaryContent);
        saveAll();
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(cache.get(Objects.requireNonNull(id)));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        Objects.requireNonNull(ids, "id must not be null");
        if(ids.isEmpty()) {
            return List.of();
        }
        List<BinaryContent> out = new ArrayList<>(ids.size());
        for(UUID uuid : ids) {
            BinaryContent binaryContent = cache.get(uuid);
            if(binaryContent != null) {
                out.add(binaryContent);
            }
        }
        return out.stream().toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        boolean existed = cache.remove(Objects.requireNonNull(id)) != null;
        if(existed) {
            saveAll();
        }
        return existed;
    }
}

 */
