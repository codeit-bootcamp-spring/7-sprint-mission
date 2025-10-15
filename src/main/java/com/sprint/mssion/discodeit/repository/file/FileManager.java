package com.sprint.mssion.discodeit.repository.file;

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
//                System.out.printf("*** %s 생성하였습니다.***", data.getClass().getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            System.out.println("생성할 필요가 없음");
        }
    }

    public Map<UUID, T> readFile() {
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object data = ois.readObject();
            return  (Map<UUID, T>) data;
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @SuppressWarnings(value = "unchecked")
    public void writeFile(Map<UUID, T> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
