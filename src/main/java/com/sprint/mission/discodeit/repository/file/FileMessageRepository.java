package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * FileMessageRepository
 * -----------------
 * 파일 입출력(File I/O)을 통해 메시지를 영구적으로 저장하고 불러오는 구현체입니다.
 *
 * Java Collection Framework(JCF)를 기반으로 메시지를 메모리(List<Message>)에 관리하며,
 * 변경 사항이 발생할 때마다 .sav 파일로 직렬화하여 데이터를 저장합니다.
 *
 * 프로그램 재시작 시 .sav 파일을 역직렬화(deserialize)하여 메시지 데이터를 복원합니다.
 *
 * 주요 기능:
 * - 메시지 생성, 조회, 삭제 등의 기본 CRUD 지원
 * - 파일 기반 직렬화/역직렬화를 통한 데이터 지속성(persistence) 보장
 *
 * 사용 파일:
 * - messages.sav : 메시지 객체들이 직렬화되어 저장되는 파일
 */
public class FileMessageRepository implements MessageRepository {
    private final List<Message> messageStore = new ArrayList<>();
    private final String filePath = AppConfig.DATA_PATH + "messages.sav";

    private FileMessageRepository() {
        loadMessagesFromFile();
    }

    private static FileMessageRepository instance = new FileMessageRepository();

    public static FileMessageRepository getInstance() {
        return new FileMessageRepository();
    }

    // --- 파일 저장 (직렬화) ---
    private void saveMessagesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messageStore);
            System.out.println("[저장 완료] 메시지 데이터 파일 저장됨");
        } catch (IOException e) {
            System.err.println("[오류] 메시지 저장 실패: " + e.getMessage());
        }
    }

    // --- 파일 불러오기 (역직렬화) ---
    private void loadMessagesFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Message> loaded = (List<Message>) ois.readObject();
            messageStore.clear();
            messageStore.addAll(loaded);
            System.out.println("[로드 완료] 메시지 데이터 로드됨");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[오류] 메시지 로드 실패: " + e.getMessage());
        }
    }

    @Override
    public void save(Message message) {
        messageStore.add(message);
        saveMessagesToFile();
    }

    @Override
    public List<Message> findAll() {
        // 외부에서 리스트를 수정하지 못하도록 복사본 반환
        return new ArrayList<>(messageStore);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return messageStore.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public void update(Message updatedMessage) {
        for (int i = 0; i < messageStore.size(); i++) {
            if (messageStore.get(i).getId().equals(updatedMessage.getId())) {
                messageStore.set(i, updatedMessage);
                saveMessagesToFile();
                break;
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        messageStore.removeIf(m -> m.getId().equals(id));
        saveMessagesToFile();
    }

    @Override
    public void deleteByUser(User user) {
        // 특정 유저가 보낸 메시지 전부 삭제
        messageStore.removeIf(m -> m.getSenderId().equals(user.getId()));
        saveMessagesToFile();
    }
}
