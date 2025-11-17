package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.*;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUtil {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final ModelType modelType;

    public FileUtil(ModelType modelType) {
        this.modelType = modelType;

        DIRECTORY = Paths.get(System.getProperty("user.dir"), "repo-data", modelType.toString());

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Path resolvePath(UUID modelID) {
        return DIRECTORY.resolve(modelID + EXTENSION);
    }

    public void saveRepository(BaseModel model) {
        Path path = resolvePath(model.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile()); //✅ implements Serializable 필요!
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            switch (modelType) {
                case USER: oos.writeObject((User)model);
                    break;
                case CHANNEL: oos.writeObject((Channel)model);
                    break;
                case MESSAGE: oos.writeObject((Message)model);
                    break;
                case USERSTATUS: oos.writeObject((UserStatus)model);
                    break;
                case READSTATUS: oos.writeObject((ReadStatus)model);
                    break;
                case BINARYCONTENT: oos.writeObject((BinaryContent)model);
                    break;
                default:
                    throw new RuntimeException("🚨Unknown type");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<BaseModel> findModel(UUID id) {
        BaseModel model = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                model = switch (modelType) {
                    case USER -> (User) ois.readObject();
                    case CHANNEL -> (Channel) ois.readObject();
                    case MESSAGE -> (Message) ois.readObject();
                    case USERSTATUS -> (UserStatus) ois.readObject();
                    case READSTATUS -> (ReadStatus) ois.readObject();
                    case BINARYCONTENT -> (BinaryContent) ois.readObject();
                    default -> throw new RuntimeException("🚨Unknown");
                };
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(model);
    }

    public List<BaseModel> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return switch (modelType) {
                                case USER -> (User) ois.readObject();
                                case CHANNEL -> (Channel) ois.readObject();
                                case MESSAGE -> (Message) ois.readObject();
                                case USERSTATUS -> (UserStatus) ois.readObject();
                                case READSTATUS -> (ReadStatus) ois.readObject();
                                case BINARYCONTENT -> (BinaryContent) ois.readObject();
                                default -> throw new RuntimeException("🚨Unknown");
                            };
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteRepository(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsRepository(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

//    @AfterAll
    public void cleanup() throws Exception {
        // 테스트 종료 후 남은 .ser 파일 제거
        Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "repo-data");
        // 삭제할 디렉토리 객체 생성
        File directoryToDelete = new File(DIRECTORY.toAbsolutePath().toString());
        // 재귀적으로 삭제
        FileSystemUtils.deleteRecursively(directoryToDelete);
    }
}
