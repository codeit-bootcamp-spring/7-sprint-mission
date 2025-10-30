package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.repository.BaseRepository;

import java.io.*;
import java.util.*;

public abstract class FileBaseRepository<T extends BaseEntity> implements BaseRepository<T> {

    /* yaml 경로
    @Value("${discodeit.repository.file-directory}")
    private String BASE_PATH;
    */

    private static final String BASE_ROOT = "/Users/apple/Desktop/codeit-7th-develop/DiscodeitUpload/";
    private final String ROOT_PATH;

    protected Map<UUID, T> data = new HashMap<>();

    public FileBaseRepository(String rooPath) {
        this.ROOT_PATH = BASE_ROOT + rooPath;
        File file = new File(ROOT_PATH).getParentFile();
        if (!file.exists()) {
            file.mkdir();       // 생성해야 할 폴더 경로가 하나일 때
        }
        this.data = loadData();
    }

    private Map<UUID,T> loadData() {
        try (ObjectInputStream ois
                     = new ObjectInputStream(
                new FileInputStream(ROOT_PATH))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (FileNotFoundException | EOFException e) {
            // 파일이 존재하지 않거나, 파일은 있지만 내용이 비어있는 경우
            // 이것은 오류가 아니라 '첫 실행' 상태이므로, 비어있는 새 Map을 반환합니다. **중요**
            return new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException("데이터 로드 실패");
        }
    }

    protected void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ROOT_PATH))){
            oos.writeObject(data);
            System.out.println("데이터 저장 성공");
        } catch (Exception e) {
            throw new RuntimeException("데이터 저장 실패");
        }
    }

    @Override
    public T save(T t){
        data.put(t.getId(), t);
        saveData();
        return t;
    }

    @Override
    public Optional<T> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    // 물리 삭제
    public void deleteById(UUID id) {
        data.remove(id);
        saveData();
    }
}
