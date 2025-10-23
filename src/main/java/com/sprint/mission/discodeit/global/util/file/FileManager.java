package com.sprint.mission.discodeit.global.util.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FileManager {


    private FileManager() {
    }

    public static <T extends Serializable> void init(Path filePath) {
        if (Files.notExists(filePath)) {
            writeFile(filePath, new HashMap<UUID, T>());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Map<UUID, T> readFile(Path filePath) {
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UncheckedIOException("파일 읽기 실패: " + filePath, new IOException(e));
        }
    }

    public static <T extends Serializable> void writeFile(Path filePath, Map<UUID, T> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(map);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

