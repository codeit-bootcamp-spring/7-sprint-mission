package com.sprint.mission.discodeit.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sprint.mission.discodeit.utils.ConfigurationLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonPersistenceManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ConfigurationLoader config;
    private final Path dataDirectoryPath;

    /**
     * 생성자를 통해 ConfigurationLoader를 주입받습니다.
     */
    public JsonPersistenceManager(ConfigurationLoader configLoader) {
        this.config = configLoader;

        String projectRoot = System.getProperty("user.dir");
        String dataSubDir = config.getProperty("data.directory", "data");
        this.dataDirectoryPath = Paths.get(projectRoot, dataSubDir);

        File dataDir = dataDirectoryPath.toFile();
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private String getDataFilePath(String key) {
        String fileName = config.getProperty(key);
        return dataDirectoryPath.resolve(fileName).toString();
    }

    public <ID, T> Map<ID, T> loadData(String key, Type type) {
        String fullPath = getDataFilePath(key);
        if (!Files.exists(Paths.get(fullPath))) {
            return new ConcurrentHashMap<>();
        }
        try (FileReader reader = new FileReader(fullPath)) {
            Map<ID, T> data = gson.fromJson(reader, type);
            return data == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(data);
        } catch (IOException e) {
            return new ConcurrentHashMap<>();
        }
    }

    public <ID, T> void saveData(String key, Map<ID, T> data) {
        String fullPath = getDataFilePath(key);
        try (FileWriter writer = new FileWriter(fullPath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("데이터 저장 중 오류 발생: " + fullPath);
            e.printStackTrace();
        }
    }

    public String getDataDirectoryPath() {
        return dataDirectoryPath.toString();
    }
}