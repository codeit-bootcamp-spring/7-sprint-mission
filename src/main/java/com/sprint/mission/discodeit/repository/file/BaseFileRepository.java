package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.transactional.TransactionContext;
import com.sprint.mission.discodeit.transactional.TransactionManager;
import com.sprint.mission.discodeit.transactional.snapshot.Rollbackable;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class BaseFileRepository<T extends Serializable> implements Rollbackable {
    private final Path DIRECTORY;
    private final Path BACKUP_DIRECTORY;
    private final String EXTENSION = ".ser";

    //생성자에서 파일데이터와 백업용 데이터 경로 지정 및 해당 경로 생성(경로에 폴더 없으면 생성)
    protected BaseFileRepository(Class<T> type, RepositoryProperties properties) {
        String basePath = properties.getFileDirectory();
        this.DIRECTORY = Paths.get(basePath, type.getSimpleName());
        this.BACKUP_DIRECTORY = Paths.get(basePath, "_backup", type.getSimpleName());
        ensureDirectoryExists(DIRECTORY, ErrorCode.DATA_DIRECTORY_CREATE_FAILED);
        ensureDirectoryExists(BACKUP_DIRECTORY, ErrorCode.BACKUP_DIRECTORY_CREATE_FAILED);
    }

    // 폴더 존재 확인 및 생성
    private void ensureDirectoryExists(Path dir, ErrorCode errorCode) {
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new CustomException(errorCode);
            }
        }
    }

    //DIRECTORY 경로 + Entity UUID + .ser 을 리턴
    protected Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }
    
    //트랜잭션이 실행 중일 경우 Backup 을 만들도록 createBackup 호출
    protected void beforeModify(){
        TransactionContext ctx = TransactionManager.getContext();

        if(ctx != null){
            ctx.registerBackup(this.getClass().getSimpleName(), this);
            createBackup();
        }
    }

    //백업 생성
    private void createBackup(){
        try(Stream<Path> files = Files.list(DIRECTORY)){
            // 현재 파일 디렉토리 내 모든 파일 복사(기존 파일 있으면 덮어 쓰는 복사)
            for (Path file : files.toList()) {
                Path backup = BACKUP_DIRECTORY.resolve(file.getFileName());
                Files.copy(file, backup, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(IOException e){
            throw new CustomException(ErrorCode.FILE_BACKUP_CREATE_FAILED);
        }
    }

    //rollback시 기존 파일들의 상태로 돌아감.
    @Override
    public void restore(){
        try(    Stream<Path> files = Files.list(DIRECTORY);
                Stream<Path> backupFiles = Files.list(BACKUP_DIRECTORY)
                ){
            //원본 디렉토리를 비우기
            files.toList().forEach(path -> {
               try {
                   Files.deleteIfExists(path);
               } catch (IOException ignored) {}
            });
            
            //백업본 복사해서 복원
            backupFiles.toList().forEach(backup -> {
                try {
                    Path restorePath = DIRECTORY.resolve(backup.getFileName());
                    Files.copy(backup, restorePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ignored) {}
            });
        } catch (IOException e){
            throw new CustomException(ErrorCode.FILE_BACKUP_RESTORE_FAILED);
        }
    }


    //파일에 Object를 저장한다.
    protected void saveToFile(UUID id, T object) {
        ensureDirectoryExists(DIRECTORY, ErrorCode.DATA_DIRECTORY_CREATE_FAILED);
        beforeModify();
        Path path = resolvePath(id);
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SAVE_FAILED);
        }
    }

    //파일에서 Object를 읽고 해당 Object 를 return 한다.
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
            throw new CustomException(ErrorCode.FILE_READ_FAILED);
        }
    }

    //파일을 삭제한다.
    protected void deleteFile(UUID id) {
        beforeModify();
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
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
            throw new CustomException(ErrorCode.FILE_READ_ALL_FAILED);
        }
        return list;
    }

    //해당 id 가 존재하는지 확인한다
    protected boolean fileExistsById(UUID id){
        return loadFromFile(id).isPresent();
    }
}
