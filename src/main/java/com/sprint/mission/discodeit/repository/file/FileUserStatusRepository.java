package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;

public class FileUserStatusRepository implements UserStatusRepository {
    //파일 저장 필요
    private final Map<UUID, UserStatus> userStatusStore = new HashMap<>();
    private final String filePath;

    public FileUserStatusRepository(String filePath) {
        this.filePath = filePath;
        loadUsersFromFile();
    }

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(userStatusStore);
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
            Map<UUID, UserStatus> loaded = (Map<UUID, UserStatus>) ois.readObject();
            userStatusStore.clear();
            userStatusStore.putAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        // 유저는 하나의 UserStatus를 가지기 때문에 key를 유저 UUID로 설정
        userStatusStore.put(userStatus.getUserId(), userStatus);
        saveUsersToFile();
    }

    @Override
    public UserStatus findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id))
                .orElseThrow(() -> new IllegalStateException("로그인 시간을 저장할 유저가 존재하지 않습니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusStore.values());
    }

    @Override
    public void updateLoginTime(UUID id) {
        UserStatus status = findById(id);
        status.setUpdatedAt();

        userStatusStore.put(id, status);
        saveUsersToFile();
    }

    @Override
    public void deleteById(UUID id) {
        userStatusStore.remove(id);
        saveUsersToFile();
    }

    @Override
    public boolean isExist(UUID userId) {
        return userStatusStore.containsKey(userId);
    }
}
