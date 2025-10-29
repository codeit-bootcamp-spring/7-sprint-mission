package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> readStatusStore = new HashMap<>();
    private final String filePath = AppConfig.DATA_PATH + "\\readstatus.sav";

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(readStatusStore);
            System.out.println("✅ read status가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("❌ read status 저장 중 오류 발생: " + e.getMessage());
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
            Map<UUID, ReadStatus> loaded = (Map<UUID, ReadStatus>) ois.readObject();
            readStatusStore.clear();
            readStatusStore.putAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatusStore.put(readStatus.getId(), readStatus);
        saveUsersToFile();
    }

    @Override
    public ReadStatus findById(UUID id) {
        return Optional.ofNullable(readStatusStore.get(id))
                .orElseThrow(() -> new IllegalStateException("저장된 정보가 없습니다."));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId));
        saveUsersToFile();
    }

    @Override
    public void deleteByChannelMember(UUID channelId, UUID memberId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId) &&  s.getUserId().equals(memberId));
        saveUsersToFile();
    }
}
