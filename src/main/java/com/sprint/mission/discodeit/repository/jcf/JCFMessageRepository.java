package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.*;

/**
 * JCFMessageRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 List<Message>를 저장소로 활용합니다.
 */
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageStore = new LinkedHashMap<>();

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
    public Message findById(UUID id) {
        return Optional.ofNullable(messageStore.get(id))
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID를 가진 메시지가 존재하지 않습니다."));
    }

    @Override
    public void update(Message updatedMessage) {
        messageStore.put(updatedMessage.getId(), updatedMessage);
    }

    @Override
    public void deleteById(UUID id) {
        Optional.ofNullable(messageStore.remove(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지가 존재하지 않습니다."));
    }

    @Override
    public void deleteByUser(UUID userId) {
        // 특정 유저가 보낸 메시지 전부 삭제
        messageStore.values().removeIf(m -> userId.equals(m.getSenderId()));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        // 채널 삭제시 채널의 모든 메시지 삭제
        messageStore.values().removeIf(m -> channelId.equals(m.getReceiverId()));
    }

    @Override
    public Instant searchLastedMessageTime(UUID channelId) {
        return findAll().stream()
                .filter(m -> channelId.equals(m.getReceiverId()) && m.getReceiveType() == ReceiveType.CHANNEL)
                .map(m -> m.getUpdatedAt())
                .sorted(Collections.reverseOrder())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("채널에 메시지가 존재하지 않습니다."));
    }
}
