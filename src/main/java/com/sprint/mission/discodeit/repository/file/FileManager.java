package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FileManager<T> {
    public T data;
    public Path filePath;

    public FileManager(Path filePath) {
        this.filePath = filePath;
        init(filePath);
    }

    public void init(Path filePath) {
        if (Files.notExists(filePath)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(new HashMap<UUID, T>());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<UUID, T> readFile() {
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object data = ois.readObject();
            return  (Map<UUID, T>) data;
        } catch (IOException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    public void writeFile(Map<UUID, T> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
