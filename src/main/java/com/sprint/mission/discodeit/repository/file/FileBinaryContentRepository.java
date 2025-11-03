package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> contentStore = new HashMap<>();
    private final String filePath;

    public FileBinaryContentRepository(String filePath) {
        this.filePath = filePath;
        loadUsersFromFile();
    }

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(contentStore);
            System.out.println("✅ 사용자 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 불러오기
    private void loadUsersFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("ℹ️ 저장된 사용자 파일이 없어 새로 생성합니다.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Map<UUID, BinaryContent> loaded = (Map<UUID, BinaryContent>) ois.readObject();
            contentStore.clear();
            contentStore.putAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(BinaryContent binaryContent) {
        contentStore.put(binaryContent.getId(), binaryContent);
        saveUsersToFile();
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(contentStore.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(contentStore.values());
    }

    @Override
    public void delete(UUID id) {
        contentStore.remove(id);
        saveUsersToFile();
    }

    @Override
    public void deleteByIds(List<UUID> idList) {
        idList.forEach(id -> {
            contentStore.remove(id);
        });

        saveUsersToFile();
    }
}
