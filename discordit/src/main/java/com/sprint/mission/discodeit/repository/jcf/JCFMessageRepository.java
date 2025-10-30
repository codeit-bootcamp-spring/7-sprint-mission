package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private static final List<Message> data = new ArrayList<>();

    @Override
    public void save(Message message) {
        data.add(message);
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<Message> findBySender(User user) {
        return data.stream()
                .filter(m -> m.getSender().equals(user))
                .toList();
    }

    @Override
    public List<Message> findByReceiver(Receivable receiver) {
        return data.stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .toList();
    }

    @Override
    public List<Message> findBySenderAndReceiver(User user, Receivable receiver) {
        return data.stream()
                .filter(m ->
                        m.getSender().equals(user)
                                && m.getReceiver().equals(receiver))
                .toList();
    }

    @Override
    public void deleteAllByReceiver(Receivable receiver) {
        data.removeIf(m -> m.getReceiver().equals(receiver));
    }

    @Override
    public void delete(Message message) {
        data.removeIf(m -> m.getUuid().equals(message.getUuid()));
    }

    @Override
    public Message findLast(Receivable receiver) {
        List<Message> foundMessages = findByReceiver(receiver);
        if (foundMessages.isEmpty()) {
            throw new IllegalStateException("저장된 메시지가 없습니다.");
        }

        return foundMessages.get(foundMessages.size() - 1);
    }

    /**
     * 테스트용 임시 메서드
     */
    private Message findById(UUID uuid) {
        return data.stream()
                .filter(m -> m.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id: " + uuid));
    }

    /**
     * 테스트용 임시 메서드
     */
    public void update(Message message) {
        Message existing = findById(message.getUuid());
        int index = data.indexOf(existing);
        if (index != -1) {
            data.set(index, message);
        }
    }

    /**
     * 테스트용 임시 메서드: 마지막으로 저장된 메시지를 반환합니다.
     */
    public Message findLast() {
        if (data.isEmpty()) {
            throw new IllegalStateException("저장된 메시지가 없습니다.");
        }
        return data.get(data.size() - 1);
    }
}
