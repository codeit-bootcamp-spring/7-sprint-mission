package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.global.utils.FileIOHandler.*;

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
    private final Map<UUID, Message> messageStore = new LinkedHashMap<>();
    private final String filePath;

    public FileMessageRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath, messageStore);
    }

    @Override
    public void save(Message message) {
        messageStore.put(message.getId(), message);
        saveToFile(filePath, messageStore);
    }

    @Override
    public List<Message> findAll() {
        // 외부에서 리스트를 수정하지 못하도록 복사본 반환
        return new ArrayList<>(messageStore.values());
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageStore.get(id));
    }

    @Override
    public void update(Message updatedMessage) {
        messageStore.replace(updatedMessage.getId(), updatedMessage);
        saveToFile(filePath, messageStore);
    }

    @Override
    public void deleteById(UUID id) {
        messageStore.remove(id);
        saveToFile(filePath, messageStore);
    }

    @Override
    public void deleteByUser(UUID userId) {
        // 특정 유저가 보낸 메시지 전부 삭제
        messageStore.values().removeIf(m -> userId.equals(m.getAuthorId()));
        saveToFile(filePath, messageStore);
    }

    @Override
    public List<UUID> deleteByChannelId(UUID channelId) {
        List<UUID> contentIds = messageStore.values().stream()
                .filter(m -> channelId.equals(m.getChannelId()))
                .flatMap(m -> m.getAttachmentIds().stream())
                .collect(Collectors.toList());

        // 채널 삭제시 채널의 모든 메시지 삭제
        messageStore.values().removeIf(m -> channelId.equals(m.getChannelId()));
        saveToFile(filePath, messageStore);
        return contentIds;
    }

    @Override
    public Instant searchLastedMessageTime(UUID channelId) {
        // 채널의 마지막 메시지를 보낸 시간 리턴
        // 메시지를 저장하지 않은 채널의 경우 null을 리턴 : Printer에서 최근 시간 대신 다른 메시지를 출력
        return findAll().stream()
                .filter(m -> channelId.equals(m.getChannelId()) && m.getReceiveType() == ReceiveType.CHANNEL)
                .map(m -> m.getUpdatedAt())
                .sorted(Collections.reverseOrder())
                .findFirst()
                .orElse(null);
    }
}
