package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseFileRepository<T extends Serializable> {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    protected BaseFileRepository(Class<T> type) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", type.getSimpleName());
        ensureDirectoryExists();
    }

    // 폴더 존재 확인 및 생성
    private void ensureDirectoryExists() {
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
            }
        }
    }

    protected Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    //파일에 Object를 저장한다.
    protected void saveToFile(UUID id, T object) {
        ensureDirectoryExists();
        Path path = resolvePath(id);
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + object.getClass().getSimpleName() + " with id=" + id, e);
        }
    }

    //파일에서 Obejct를 읽고 해당 Object 를 return 한다.
    protected Optional<T> loadFromFile(UUID id) {
        Path path = resolvePath(id);

        if (!Files.exists(path)) {return Optional.empty();}

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            T obj = (T) ois.readObject();
            return Optional.ofNullable(obj); // 혹시 모를 null 대응
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    //파일을 삭제한다.
    protected void deleteFile(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + resolvePath(id), e);
        }
    }

    //모든 파일들을 읽는다.
    protected List<T> findAllFiles() {
        List<T> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(DIRECTORY, "*" + EXTENSION)) {
            for (Path path : stream) {
                UUID id = UUID.fromString(
                        path.getFileName().toString().replace(EXTENSION, "")
                );
                loadFromFile(id).ifPresent(list::add);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read all files in " + DIRECTORY, e);
        }
        return list;
    }

    //해당 id 가 존재하는지 확인한다
    protected boolean fileExistsById(UUID id){
        return loadFromFile(id).isPresent();
    }
}
