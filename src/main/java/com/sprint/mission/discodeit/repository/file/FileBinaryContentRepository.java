package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String BINARY_DATA_FILE = "binaryData.ser";
    private final String ROOT_PATH;

    protected Map<UUID, BinaryContent> data = new HashMap<>();

    public FileBinaryContentRepository(String path) {
        this.ROOT_PATH = path + "/" + BINARY_DATA_FILE;
        File file = new File(ROOT_PATH).getParentFile();
        if (!file.exists()) {
            file.mkdir();       // 생성해야 할 폴더 경로가 하나일 때
        }
        this.data = loadData();
    }

    private Map<UUID,BinaryContent> loadData() {
        try (ObjectInputStream ois
                     = new ObjectInputStream(
                new FileInputStream(ROOT_PATH))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
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
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        saveData();
        return binaryContent;
    }

    // -------------------------------------------------------------------

    @Override
    public Optional<BinaryContent> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    // 물리 삭제
    public void deleteById(UUID id) {
        data.remove(id);
        saveData();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream().map(data::get)
                .collect(Collectors.toList());
    }
}
