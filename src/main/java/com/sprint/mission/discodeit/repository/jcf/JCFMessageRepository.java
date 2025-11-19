package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * JCFMessageRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 List<Message>를 저장소로 활용합니다.
 */
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageStore = new ConcurrentHashMap<>();

    @Override
    public void save(Message message) {
        messageStore.put(message.getId(), message);
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
    }

    @Override
    public void deleteById(UUID id) {
        messageStore.remove(id);
    }

    @Override
    public void deleteByUser(UUID userId) {
        // 특정 유저가 보낸 메시지 전부 삭제
        messageStore.values().removeIf(m -> userId.equals(m.getAuthorId()));
    }

    @Override
    public List<UUID> deleteByChannelId(UUID channelId) {
        List<UUID> contentIds = messageStore.values().stream()
                .filter(m -> channelId.equals(m.getChannelId()))
                .flatMap(m -> m.getAttachmentIds().stream())
                .collect(Collectors.toList());

        // 채널 삭제시 채널의 모든 메시지 삭제
        messageStore.values().removeIf(m -> channelId.equals(m.getChannelId()));
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
                .get();
    }
}
